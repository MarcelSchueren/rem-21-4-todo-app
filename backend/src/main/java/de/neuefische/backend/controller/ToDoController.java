package de.neuefische.backend.controller;

import de.neuefische.backend.model.ApiTask;
import de.neuefische.backend.model.Task;
import de.neuefische.backend.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<List<Task>> listTasks() {
        return ResponseEntity.ok().body(toDoService.showAllList());
    }

    @PostMapping
    public ResponseEntity<Task> postTask(@RequestBody ApiTask apiTask) {
        if (apiTask.getDescription().equals("")) {
            return ResponseEntity.badRequest().build();
        } else {
            Task taskToAdd = toDoService.add(apiTask);
            URI location = URI.create("api/todo/" + taskToAdd.getId());
            return ResponseEntity.created(location).body(taskToAdd);
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable String id) {
        Optional<Task> taskToDelete = toDoService.deleteTask(id);
        if (taskToDelete.isPresent()) {
            return ResponseEntity.accepted().body(taskToDelete.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Task> findID(@PathVariable String id) {
        Optional<Task> response = toDoService.findId(id);
        if (response.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        } else return ResponseEntity.ok().body(response.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id) {
        Optional<Task> response = toDoService.findId(id);
        if (response.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        Task updatedTask = toDoService.changeStatus(response.get());
        return ResponseEntity.accepted().body(updatedTask);
    }
}
