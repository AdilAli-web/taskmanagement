# Task Management REST API

A REST API for managing tasks, built with Java 21, Spring Boot, Spring
Data JPA, Hibernate, Maven, and MySQL. The application can run locally
or in Docker.

## Features

-   CRUD operations for tasks
-   Task fields: `id`, `title`, `description`, `status`, and `dueDate`
-   Controller--Service--Repository structure
-   JPA/Hibernate persistence
-   Spring Security configuration
-   Docker support for the application and MySQL

> **Assignment note:** The assignment specifically asks for a PostgreSQL
> schema or dump. This project is currently configured for MySQL.
> Confirm whether MySQL is acceptable, or migrate to PostgreSQL and
> provide a PostgreSQL schema/dump before submitting.

## Technology stack

-   Java 21
-   Spring Boot
-   Spring Data JPA / Hibernate
-   MySQL 8.4
-   Maven
-   Docker

## Task API

The task controller base path is `/api/task`.

  Method     Endpoint           Purpose
  ---------- ------------------ -----------------------
  `POST`     `/api/task`        Create a task
  `GET`      `/api/task`        Retrieve tasks
  `GET`      `/api/task/{id}`   Retrieve a task by ID
  `PUT`      `/api/task/{id}`   Update a task
  `DELETE`   `/api/task/{id}`   Delete a task

Confirm the exact routes against `TaskController` before submitting.

### Example: create a task

`POST http://localhost:8080/api/task`

``` json
{
  "title": "Learn Spring Boot Security",
  "description": "Understand JWT authentication and authorization",
  "status": "PENDING",
  "dueDate": "2026-10-20"
}
```

Use status values supported by your task status enum. If the endpoint is
protected, authenticate using the method configured in the project.

## Prerequisites

For local development: - Java 21 - Maven (or the Maven Wrapper, if
included) - MySQL 8.4

For Docker: - Docker Desktop or Docker Engine

## Configuration

For running the application directly on your computer, if MySQL is
published on host port `3307`, configure
`src/main/resources/application.properties` like this:

``` properties
spring.datasource.url=jdbc:mysql://localhost:3307/task_management?createDatabaseIfNotExist=true
spring.datasource.username=taskapp
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

jwt.secret=${JWT_SECRET}
jwt.expiration=600
```

Set `DB_PASSWORD` and `JWT_SECRET` in your environment. Do not commit
real passwords, tokens, or signing secrets to GitHub. Use a strong,
unique JWT secret if JWT is enabled.

For the Dockerized application, the JDBC URL should use the MySQL
container hostname:

`jdbc:mysql://taskmanagement-mysql:3306/task_management?createDatabaseIfNotExist=true`

Both containers must share a Docker network.

## Run locally

1.  Start MySQL and create a database named `task_management`.

2.  Configure the datasource URL, username, and password.

3.  From the project root, build the project:

    ``` bash
    mvn clean package
    ```

4.  Start the application:

    ``` bash
    mvn spring-boot:run
    ```

5.  The API should be available at `http://localhost:8080`.

If the project includes `mvnw.cmd`, you can use that wrapper on Windows
instead of installing Maven globally.

## Run with Docker

Run these commands from the folder containing the `Dockerfile` and
`pom.xml`.

### 1. Create the network

``` powershell
docker network create taskmanagement-network
```

If it already exists, continue.

### 2. Start MySQL

Choose a strong password. The named volume preserves database files
across container recreation.

``` powershell
docker run -d --name taskmanagement-mysql --network taskmanagement-network --network-alias taskmanagement-mysql -p 3307:3306 -e MYSQL_ROOT_PASSWORD="CHANGE_ME_ROOT_PASSWORD" -e MYSQL_DATABASE=task_management -v mysql_data:/var/lib/mysql mysql:8.4
```

For the application, prefer a separate MySQL user instead of using
`root`. Log in to MySQL and run:

``` sql
CREATE USER 'taskapp'@'%' IDENTIFIED BY 'CHANGE_ME_APP_PASSWORD';
GRANT ALL PRIVILEGES ON task_management.* TO 'taskapp'@'%';
```

If `mysql_data` already existed, the environment variables may not reset
the existing password or accounts.

### 3. Build the application image

``` powershell
docker build -t taskmanagement:1.0 .
```

### 4. Run the application container

Use the same app-user password you configured in MySQL:

``` powershell
docker run -d --name taskmanagement-app --network taskmanagement-network -p 8080:8080 -e SPRING_DATASOURCE_URL="jdbc:mysql://taskmanagement-mysql:3306/task_management?createDatabaseIfNotExist=true" -e SPRING_DATASOURCE_USERNAME=taskapp -e SPRING_DATASOURCE_PASSWORD="CHANGE_ME_APP_PASSWORD" -e JWT_SECRET="CHANGE_ME_TO_A_STRONG_SECRET" taskmanagement:1.0
```

### 5. Check status and logs

``` powershell
docker ps
docker logs -f taskmanagement-app
```

Both the application and database containers should be running. Press
`Ctrl+C` to stop following logs; this does not stop the container.

Useful commands:

``` powershell
docker images
docker ps -a
docker logs taskmanagement-app
docker logs taskmanagement-mysql
docker stop taskmanagement-app
docker start taskmanagement-app
```

Do not remove the `mysql_data` volume unless you intentionally want to
delete the stored database data.

## Tests

Run the test suite with:

``` bash
mvn test
```

The assignment requests JUnit/Mockito tests and targets at least 80%
code coverage. Review the test results and measure coverage before
claiming a percentage.

## Database schema / dump

The current project uses MySQL. To export a MySQL dump, run:

``` powershell
docker exec taskmanagement-mysql mysqldump -uroot -p task_management > task_management.sql
```

Enter the MySQL password when prompted. This creates a **MySQL** dump,
not a PostgreSQL dump. The assignment explicitly requests PostgreSQL, so
confirm that MySQL is acceptable or provide a PostgreSQL version if
required.

## Push the Docker image to Docker Hub

Replace `YOUR_DOCKERHUB_USERNAME` with your Docker Hub username:

``` powershell
docker login
docker tag taskmanagement:1.0 YOUR_DOCKERHUB_USERNAME/taskmanagement:1.0
docker push YOUR_DOCKERHUB_USERNAME/taskmanagement:1.0
```


