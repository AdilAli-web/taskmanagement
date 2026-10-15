package com.example.taskmanagement.Exception;

import java.time.LocalDateTime;

public record ExceptionResponse(LocalDateTime timestamp,
                                int status,
                                String error,
                                String message,
                                String path) {
}
