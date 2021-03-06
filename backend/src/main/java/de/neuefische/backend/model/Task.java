package de.neuefische.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Task {
    private String id;
    private String description;
    private TaskStatus status;          // OPEN, IN_PROGRESS, DONE

}
