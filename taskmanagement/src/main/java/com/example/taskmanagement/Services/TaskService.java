package com.example.taskmanagement.Services;

import com.example.taskmanagement.Dto.CreateTaskRequest;
import com.example.taskmanagement.Dto.TaskResponse;
import com.example.taskmanagement.Dto.UpdateTaskRequest;
import com.example.taskmanagement.Entity.Task;
import com.example.taskmanagement.Entity.TaskStatus;
import com.example.taskmanagement.Exception.TaskNotFoundException;
import com.example.taskmanagement.Repo.TaskRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private TaskRepo repo;

    public TaskService(TaskRepo repo) {
        this.repo = repo;
    }

//    Creating Task Service
@Transactional
    public TaskResponse createTask(@Valid CreateTaskRequest request) {
      String title=request.getTitle().trim();
//      String description=request.getDescription();


        if(request.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Due date cannot be in the past"
            );
        }
        Task task=new Task();

        task.setTitle(title);
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());


        task.setStatus(
                request.getStatus() != null
                        ? request.getStatus()
                        : TaskStatus.PENDING
        );


        Task savedTask = repo.save(task);

      return new TaskResponse(savedTask.getId(),savedTask.getTitle(),savedTask.getDescription(),savedTask.getStatus(),savedTask.getDueDate(),savedTask.getVersion());

    }


//service layer of getting all task at once
@Transactional
 public  List<TaskResponse> getAllTasks() {
        List<Task> task=repo.findAll();
        List<TaskResponse> response=new ArrayList<>();
        for(Task t:task){
            response.add(
                    new TaskResponse(t.getId(),t.getTitle(),t.getDescription(),t.getStatus(),t.getDueDate(),t.getVersion())
            );
        }
        return response;
    }

//    get task by id
@Transactional
    public TaskResponse getTaskById(Long id) {
        Task task=repo.findById(id).orElseThrow(()->
                new TaskNotFoundException("Task with id "+id+" not found")
                );
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getDueDate(), task.getVersion());
    }


    @Transactional
    public  TaskResponse updateTask(Long id, @Valid UpdateTaskRequest request) {
        Task task=repo.findById(id).orElseThrow(()->
                new TaskNotFoundException("Task with id "+id+" not found"));
        if(task.getStatus()==null) {
            throw new IllegalArgumentException();
        }
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setStatus(request.getStatus());

        Task savedTask=repo.save(task);

        return new TaskResponse(savedTask.getId(),savedTask.getTitle(),savedTask.getDescription(),savedTask.getStatus(),savedTask.getDueDate(),savedTask.getVersion());

    }

//deleting a task
    @Transactional
    public void deleteTask(Long id) {

        Task task = repo.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + id
                        )
                );
        repo.delete(task);
    }

}
