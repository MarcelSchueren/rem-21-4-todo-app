package de.neuefische.backend.controller;
import de.neuefische.backend.model.ApiTask;
import de.neuefische.backend.model.Task;
import de.neuefische.backend.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/todo")
public class ToDoController {

    private final ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping
    public List<Task> listTasks() {
        return toDoService.showAllList();
    }

    @PostMapping
    public ResponseEntity<Object> postTask(@RequestBody ApiTask apiTask) {
        return toDoService.add(apiTask);
    }

    @DeleteMapping(path = "{id}")
    public void deleteTask(@PathVariable String id){
        toDoService.deleteTask(id);
    }

    @GetMapping("{id}")
    public Task findID(@PathVariable String id) {
        Optional<Task> response = toDoService.findId(id);
        if (response.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "taskmitid" + id + "nicht gefunden");
        }
        return response.get();
    }

    @PutMapping("{id}")
    public void updateTask(@PathVariable String id){
        Optional<Task> response = toDoService.findId(id);
        if (response.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "taskmitid" + id + "nicht gefunden");
        }
       toDoService.changeStatus(response.get());
    }


}
