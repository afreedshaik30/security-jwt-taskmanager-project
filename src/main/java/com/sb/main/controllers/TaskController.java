package com.sb.main.controllers;

import com.sb.main.DTO.TaskDto;
import com.sb.main.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController
{

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

     @PostMapping("/{userid}")
     public ResponseEntity<TaskDto> saveTask(@PathVariable(name = "userid") long uid,
                                             @RequestBody TaskDto taskDto)
     {
         TaskDto savedTask = taskService.saveTaskByUserId(uid, taskDto);
         return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
     }

     @GetMapping("/{userid}/all")
     public  ResponseEntity<List<TaskDto>> getAllTasksByUserId(@PathVariable(name="userid") long uid)
     {
         List<TaskDto> tasks = taskService.getAllTasksByUserId(uid);
         return new ResponseEntity<>(tasks,HttpStatus.OK);
     }

     @GetMapping("/uid/{userid}/tid/{taskid}")
     public ResponseEntity<TaskDto> getTaskByUserId(@PathVariable("userid") long uid,
                                                    @PathVariable("taskid") long tid)
     {
         TaskDto task = taskService.getTaskByUserId(uid, tid);
         return new ResponseEntity<>(task,HttpStatus.OK);
     }

     @DeleteMapping("/uid/{userid}/tid/{taskid}")
     public ResponseEntity<String> deleteTaskByUserId(@PathVariable("userid") long uid,
                                                       @PathVariable("taskid") long tid)
     {
         taskService.deleteTaskByUserId(uid, tid);
         return new ResponseEntity<>("TASK DELETED Successfully!!!",HttpStatus.OK);
     }
}
