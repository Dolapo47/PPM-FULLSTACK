package com.example.demo.services;

import com.example.demo.exceptions.UsernameAlreadyExistsException;
import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //username must be unique
            newUser.setUsername(newUser.getUsername());
            //make sure password and confirm match
            // we dont persist or show the confirm password
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        }catch (Exception ex){
            throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername()+"' already exists");
        }

    }
}
