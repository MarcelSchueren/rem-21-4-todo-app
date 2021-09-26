package de.neuefische.backend.repo;

import de.neuefische.backend.model.ApiTask;
import de.neuefische.backend.model.Task;
import org.springframework.stereotype.Repository;

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

    public Task add(ApiTask apiTask) {
        Task task = mapFromApiTaskToTask(apiTask);
        tasks.add(task);
        return task;
    }

    private Task mapFromApiTaskToTask(ApiTask apiTask) {
        String id = generateUUID();
        return new Task(id, apiTask.getDescription(), apiTask.getStatus());
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

    public Optional<Task> deleteTask(String id) {
        Task taskToDelete;
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                taskToDelete = task;
                tasks.remove(task);
                return Optional.of(taskToDelete);
            }
        }
        return Optional.empty();
    }
}

