package de.neuefische.backend.controller;

import de.neuefische.backend.model.ApiTask;
import de.neuefische.backend.model.Task;
import de.neuefische.backend.model.TaskStatus;
import de.neuefische.backend.repo.ToDoRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ToDoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ToDoRepo toDoRepo;

    @BeforeEach
    public void clearRepo() {
        toDoRepo.clearRepo();
    }

    @Test
    void listTasksShouldReturnTwoTasks() {
        //GIVEN
        String url = "http://localhost:" + port + "/api/todo";
        ApiTask apiTask1 = new ApiTask("Task 1", "OPEN");
        ApiTask apiTask2 = new ApiTask("Task 2", "OPEN");
        Task task1 = toDoRepo.add(apiTask1);
        Task task2 = toDoRepo.add(apiTask2);
        //WHEN
        ResponseEntity<Task[]> response = restTemplate.getForEntity(url, Task[].class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), arrayContainingInAnyOrder(task1, task2));
    }

    @Test
    void postTaskShouldReturnOneTask() {
        String url = "http://localhost:" + port + "/api/todo";
        ApiTask apiTask1 = new ApiTask("Task 1", "OPEN");
        //WHEN
        ResponseEntity<Task> response = restTemplate.postForEntity(url, apiTask1 ,Task.class);
        //THEN
        String task1Id = toDoRepo.showAllTasks().get(0).getId();
        Task task1;
        if (toDoRepo.findId(task1Id).isPresent()) {
            task1 = toDoRepo.findId(task1Id).get();
        } else {
            task1 = new Task("FALSCHE ID", "Falsche Beschreibung", TaskStatus.DONE);
        }
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertEquals(response.getBody(), task1);
    }

    @Test
    void deleteTaskShouldReturnTwoTasks() {
        //GIVEN
        ApiTask apiTask1 = new ApiTask("Task 1", "OPEN");
        ApiTask apiTask2 = new ApiTask("Task 2", "OPEN");
        ApiTask apiTask3 = new ApiTask("Task 3", "OPEN");
        Task task1 = toDoRepo.add(apiTask1);
        Task task2 = toDoRepo.add(apiTask2);
        Task task3 = toDoRepo.add(apiTask3);
        String urlOfTaskToDelete = "http://localhost:" + port + "/api/todo/" + task2.getId();
        //WHEN
        restTemplate.delete(urlOfTaskToDelete);
        String url = "http://localhost:" + port + "/api/todo";
        ResponseEntity<Task[]> response = restTemplate.getForEntity(url, Task[].class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), arrayContainingInAnyOrder(task1, task3));
    }

    @Test
    void updateTaskShouldReturnUpdatedTasks() {
        //GIVEN
        ApiTask apiTask1 = new ApiTask("Task 1", "OPEN");
        ApiTask apiTask2 = new ApiTask("Task 2", "OPEN");
        Task task1 = toDoRepo.add(apiTask1);
        Task task2 = toDoRepo.add(apiTask2);
        String urlTask1 = "http://localhost:" + port + "/api/todo/" + task1.getId();
        String urlTask2 = "http://localhost:" + port + "/api/todo/" + task2.getId();

        //WHEN
        restTemplate.put(urlTask1, task1);
        restTemplate.put(urlTask1, task1);
        restTemplate.put(urlTask2, task2);
        String url = "http://localhost:" + port + "/api/todo";
        ResponseEntity<Task[]> response = restTemplate.getForEntity(url, Task[].class);

        //THEN
        assertThat(response.getBody(), arrayContainingInAnyOrder(
                new Task(task1.getId(), "Task 1", TaskStatus.DONE),
                new Task(task2.getId(), "Task 2", TaskStatus.IN_PROGRESS)
        ));
    }

    @Test
    void findIDShouldReturnCorrectTask() {
        //GIVEN
        ApiTask apiTask1 = new ApiTask("Task 1", "OPEN");
        Task task1 = toDoRepo.add(apiTask1);
        String idTask1 = task1.getId();
        String url = "http://localhost:" + port + "/api/todo/" + idTask1;
        //WHEN
        ResponseEntity<Task> response = restTemplate.getForEntity(url, Task.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertEquals(response.getBody(), task1);
    }

    @Test
    void findIDShouldReturnBadRequest() {
        //GIVEN
        ApiTask apiTask1 = new ApiTask("Task 1", "OPEN");
        Task task1 = toDoRepo.add(apiTask1);
        String falseTask1ID = "falseID";
        String url = "http://localhost:" + port + "/api/todo/" + falseTask1ID;
        //WHEN
        ResponseEntity<Task> response = restTemplate.getForEntity(url, Task.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

}