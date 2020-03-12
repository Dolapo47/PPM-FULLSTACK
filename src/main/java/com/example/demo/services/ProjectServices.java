package com.example.demo.services;

import com.example.demo.exceptions.ProjectIdException;
import com.example.demo.exceptions.ProjectNotFoundException;
import com.example.demo.model.Backlog;
import com.example.demo.model.Project;
import com.example.demo.model.ProjectTask;
import com.example.demo.model.User;
import com.example.demo.repositories.BacklogRepository;
import com.example.demo.repositories.ProjectRepository;
import com.example.demo.repositories.ProjectTaskRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServices {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){
        try{

            if(project.getId() != null){
                Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
                if(existingProject !=null && (!existingProject.getProjectLeader().equals(username))){
                    throw new ProjectNotFoundException("Project not found in your account");
                }else if(existingProject == null){
                    throw new ProjectNotFoundException("Project with ID: '"+project.getProjectIdentifier()+"' cannot be updated");
                }
            }

            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier((project.getProjectIdentifier().toUpperCase()));

            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            if(project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project id '"+project
                    .getProjectIdentifier().toUpperCase()+"' already exists");
        }

    }

    public Project findProjectByIdentifier(String projectId, String username){
        Project project = projectRepository.findByProjectIdentifier(projectId);
        if(project == null){
            throw new ProjectIdException("Project id '"+projectId+"' does not exist");
        }
        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username){
        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }

    Iterable<ProjectTask>findBacklogById(String id){
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
}
