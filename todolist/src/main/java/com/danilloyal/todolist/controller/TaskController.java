package com.danilloyal.todolist.controller;

import com.danilloyal.todolist.model.TaskModel;
import com.danilloyal.todolist.model.TaskNotFoundException;
import com.danilloyal.todolist.model.AccessUnauthorizedException;
import com.danilloyal.todolist.model.NullPathException;
import com.danilloyal.todolist.repository.TaskRepository;
import com.danilloyal.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;

    @PostMapping("/create")
    public ResponseEntity<TaskModel> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        taskModel.setUserId((UUID)userId);
        var task0 = taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(task0);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskModel>> findAllTasks(HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var allTasks = taskRepository.findByUserId((UUID) userId);
        return ResponseEntity.ok().body(allTasks);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TaskModel> update(@RequestBody TaskModel taskModel, @PathVariable UUID id,  HttpServletRequest request) throws RunTimeException {
        if(id == null)throw new NullPathException("Valid ID required");
        var task = this.taskRepository.findById(id).orElseThrow(() => new TaskNotFoundException("Task not found"));

        Utils.copyNonNullProperties(taskModel, task);

        var userId = request.getAttribute("userId");
        if(!task.getUserId().equals(userId)){
            throw new AccessUnauthorizedException("Change unauthorized");
        }

        return ResponseEntity.ok().body(this.taskRepository.save(task));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete (@PathVariable UUID id, HttpServletRequest request) throws RunTimeException{
        if(id == null)throw new NullPathException("Valid ID required");

        var task = this.taskRepository.findById(id).orElseThrow(() => new TaskNotFoundException("Task not found"));
        
        var userId = request.getAttribute("userId");
        if(!task.getUserId().equals(userId)){
            throw new AccessUnauthorizedException("Change unauthorized");
        }
        taskRepository.deleteById(id);
        return ResponseEntity.ok().body("Task deleted");
    }
}
