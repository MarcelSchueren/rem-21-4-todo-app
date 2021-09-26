package de.neuefische.backend.service;

import de.neuefische.backend.model.ApiTask;
import de.neuefische.backend.model.Task;
import de.neuefische.backend.repo.ToDoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {
    private final ToDoRepo toDoRepo;

    @Autowired
    public ToDoService(ToDoRepo toDoRepo) {
        this.toDoRepo = toDoRepo;
    }

    public List<Task> showAllList() {
        return toDoRepo.showAllTasks();
    }

    public Task add(ApiTask apiTask) {
        return toDoRepo.add(apiTask);
    }

    public Optional<Task> findId(String id) {
        return toDoRepo.findId(id);
    }

    public Task changeStatus(Task task) {
        return toDoRepo.changeStatus(task);
    }

    public Optional<Task> deleteTask(String id) {
        return toDoRepo.deleteTask(id);
    }
}
