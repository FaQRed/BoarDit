# BoarDit

BoarDit is an An educational CRUD web project divided into two parts: API and Frontend with JWT authorization and BoardGamesGeek API using, that allows users to manage a catalog of board games.

## API
The API is built using:
- Java
- Hibernate
- Spring Security
- MySQL
- JWT
- Lombok

It follows a multi-module architecture with the following modules:

- **boardit-boardgames-client**: Responsible for fetching board game data from the BoardGamesGeek API.
- **boardit-data**: Contains database table models and repositories.
- **boardit-updater**: Handles logic for storing data from the API into the database.
- **boardit-web-api**: Provides controllers for frontend CRUD operations and user registration/login.

The API also includes exception handling and has a test coverage of over 80%.

## Database Setup
To run the MySQL database, start a Docker container using the following command:
```sh
 docker run --name boardit-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=boardit -e MYSQL_USER=admin -e MYSQL_PASSWORD=faqred -p 3306:3306 -d mysql:latest
```

### Database Configuration (`application.properties`):
```properties
spring.application.name=boardit-data
spring.jpa.show-sql=true

spring.datasource.url=jdbc:mysql://localhost:3306/boardit?serverTimezone=UTC&useSSL=false
spring.datasource.username=admin
spring.datasource.password=faqred
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.datasource.name=boardit

spring.jpa.properties.javax.persistence.schema-generation.scripts.drop-source=metadata
spring.jpa.properties.javax.persistence.schema-generation.scripts.drop-target=drop.sql
```

## Frontend
The frontend is built using:
- Thymeleaf
- Bootstrap
- JavaScript

## Admin panel

The project includes an admin panel where administrators can perform CRUD operations on:

Users (manage roles and status such as active/banned)
Board games (manage extensions)

Board games
---
### Setup Instructions
1. Clone the repository:
   ```sh
   git clone https://github.com/FaQRed/BoarDit.git
   ```
2. Configure your database settings in `application.properties`.
3. Build and run the project using Maven or your preferred build tool.
4. Access the frontend via the configured URL.

### Features
- Fetch and store board game data from BoardGamesGeek API.
- Perform CRUD operations on board game data.
- User authentication and authorization using JWT.
- Exception handling and high test coverage.

