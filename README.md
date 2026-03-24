# Digital Notice Board Backend

Backend service for the Digital Notice Board platform.

This project provides APIs for notice management, user operations, notifications, authentication, and media integration.

## Overview

| Area | Details |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 3.1.5 |
| Database | MySQL (default), PostgreSQL driver available |
| API Docs | Springdoc OpenAPI (Swagger UI) |
| Logging | Log4j2 |
| Build Tool | Maven Wrapper |

## Core Capabilities

- Notice create, update, fetch, and filtering workflows
- User and authentication-related APIs
- User notification endpoints
- Email integration support
- Cloudinary integration for media handling

## Project Layout

~~~text
src/main/java/digital_board/digital_board
|- Config/           # security, mail, cloudinary, swagger config
|- Controller/       # REST endpoints
|- Dto/              # request and response DTOs
|- Entity/           # JPA entities
|- Exception/        # global and custom exceptions
|- Repository/       # data access layer
|- ServiceImpl/      # business logic
`- Servies/          # service interfaces

src/main/resources
`- application.yml   # runtime configuration
~~~

## Prerequisites

- JDK 17 or above
- MySQL instance (default setup expects port 3307)
- Docker (optional, for containerized run)

## Quick Start

### 1) Configure Environment

Use environment variables instead of hardcoding credentials.

~~~powershell
$env:SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3307/digital_board"
$env:SPRING_DATASOURCE_USERNAME="root"
$env:SPRING_DATASOURCE_PASSWORD="root"
$env:SERVER_PORT="9595"
~~~

### 2) Run Application

Windows (PowerShell):

~~~powershell
.\mvnw.cmd clean spring-boot:run
~~~

Linux/macOS:

~~~bash
./mvnw clean spring-boot:run
~~~

Application URL:

- http://localhost:9595

### 3) Access API Docs

- http://localhost:9595/swagger-ui/index.html

## Build and Test

Build jar:

~~~powershell
.\mvnw.cmd clean package
~~~

Run tests:

~~~powershell
.\mvnw.cmd test
~~~

Build output:

- target/App.jar

## Docker

Build image:

~~~powershell
docker build -t digital-notice-board-backend .
~~~

Run container:

~~~powershell
docker run --rm -p 9595:9595 digital-notice-board-backend
~~~

## API Modules

Controllers currently present:

- NoticeController
- UserController
- UserNotificationController

Use Swagger UI to inspect endpoint paths, payloads, and response models.

## Security and Configuration Notes

- Do not commit live credentials (DB, OAuth, Cloudinary, mail) to source control.
- Keep generated build output under target/ ignored.
- Prefer profile-based config for dev, staging, and production environments.
