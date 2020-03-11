package com.example.demo.web;

import com.example.demo.model.Project;
import com.example.demo.services.MapValidationErrorService;
import com.example.demo.services.ProjectServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/project")
public class ProjectController {
    @Autowired
    private ProjectServices projectServices;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal){

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        Project project1 = projectServices.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<>(project1, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?>getProjectById(@PathVariable String projectId, Principal principal){
        Project project = projectServices.findProjectByIdentifier(projectId.toUpperCase(), principal.getName());
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal){
        return projectServices.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
        projectServices.deleteProjectByIdentifier(projectId.toUpperCase(), principal.getName());
        return new ResponseEntity<String>("Project with Id: '"+projectId+"' was deleted", HttpStatus.OK);
    }
}
