package com.example.demo.services;

import com.example.demo.model.Project;
import com.example.demo.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServices {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        return projectRepository.save(project);
    }

}
