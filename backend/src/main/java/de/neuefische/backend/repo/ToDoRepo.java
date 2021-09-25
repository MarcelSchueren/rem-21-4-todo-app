package de.neuefische.backend.repo;

import de.neuefische.backend.model.ApiTask;
import de.neuefische.backend.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ToDoRepo {

    private final List<Task> tasks = new ArrayList<>();

    public List<Task> showAllTasks() {
        return this.tasks;
    }

    public ResponseEntity<Object> add(ApiTask apiTask) {
        String id = generateUUID();
        URI location = URI.create("api/todo/" + id);
        if (apiTask.getDescription().equals("")) {
            return ResponseEntity.badRequest().body(null);
        } else {
            Task task = new Task(id, apiTask.getDescription(), apiTask.getStatus());
            tasks.add(task);
            return ResponseEntity.created(location).body(apiTask);
        }
    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public Optional<Task> findId(String id) {

        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    public void changeStatus(Task task) {
        if (task.getStatus().equals("OPEN")) {
            task.setStatus("IN_PROGRESS");
        } else if (task.getStatus().equals("IN_PROGRESS")) {
            task.setStatus("DONE");
        }
    }

    public ResponseEntity<Object> deleteTask(String id) {
        Task taskToDelete;
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                taskToDelete = task;
                tasks.remove(task);
                return ResponseEntity.accepted().body(taskToDelete);
            }
        }
        return ResponseEntity.badRequest().body(null);
    }
}

