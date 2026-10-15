package com.example.taskmanagement.Dto;

import com.example.taskmanagement.Entity.TaskStatus;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDate dueDate,
        Long version
) {
}
