package com.example.taskmanagement.Controller;

import com.example.taskmanagement.Dto.CreateTaskRequest;
import com.example.taskmanagement.Dto.TaskResponse;
import com.example.taskmanagement.Dto.UpdateTaskRequest;
import com.example.taskmanagement.Services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
//    create a task first

    @PostMapping
    public ResponseEntity<TaskResponse> createTask( @Valid @RequestBody CreateTaskRequest request){
TaskResponse response= taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

//    get all task
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {

        return ResponseEntity.ok(taskService.getAllTasks());
    }

//get specific task by id

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@RequestParam Long id){
        TaskResponse response=taskService.getTaskById(id);
//        return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }



//  updating a task
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {

        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

//    deleting a task by id

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }
}
