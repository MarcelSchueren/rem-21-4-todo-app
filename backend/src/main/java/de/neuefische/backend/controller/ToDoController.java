package de.neuefische.backend.controller;

import de.neuefische.backend.model.ApiTask;
import de.neuefische.backend.model.Task;
import de.neuefische.backend.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> deleteTask(@PathVariable String id) {
        return toDoService.deleteTask(id);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findID(@PathVariable String id) {
        Optional<Task> response = toDoService.findId(id);
        if (response.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        } else return ResponseEntity.accepted().body(response.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateTask(@PathVariable String id) {
        Optional<Task> response = toDoService.findId(id);
        if (response.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        toDoService.changeStatus(response.get());
        return ResponseEntity.accepted().body(null);
    }
}
