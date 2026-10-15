
CREATE DATABASE IF NOT EXISTS task_management;

USE task_management;

CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    due_date DATE,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_tasks_status ON tasks (status);
