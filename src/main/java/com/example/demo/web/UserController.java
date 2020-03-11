package com.example.demo.web;

import com.example.demo.model.User;
import com.example.demo.payload.JwtLoginSuccessResponse;
import com.example.demo.payload.LoginRequest;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.services.MapValidationErrorService;
import com.example.demo.services.UserService;
import com.example.demo.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.demo.security.SecurityConstant.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private AuthenticationManager authenticationmanager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?>authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?>errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;
        Authentication authentication = authenticationmanager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX+jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtLoginSuccessResponse(true, jwt));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUSer(@Valid @RequestBody User user, BindingResult result){
        //validate passwords match

        userValidator.validate(user, result);
        ResponseEntity<?>errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        User newUser = userService.saveUser(user);
        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }

}
