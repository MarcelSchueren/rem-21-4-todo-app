package de.neuefische.backend.service;

import de.neuefische.backend.model.ApiTask;
import de.neuefische.backend.model.Task;
import de.neuefische.backend.repo.ToDoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<Object> add(ApiTask apiTask) {
        return toDoRepo.add(apiTask);
    }

  public Optional<Task> findId(String id) {
        return toDoRepo.findId(id);
  }

    public void changeStatus(Task task) {
        toDoRepo.changeStatus(task);
    }


    public ResponseEntity<Object> deleteTask(String id) {
        return toDoRepo.deleteTask(id);
    }
}
