# Cabonerf Backend Java Spring Boot Setup

---

This guide will help you set up the Backend Java Spring Boot environment for the Cabonerf project. Follow the instructions step-by-step to install and configure the necessary components.

# Requirements

---

Before you start, ensure you have the following installed on your system:
- [IntelliJ](https://www.jetbrains.com/idea/download/?section=windows)
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)

# Getting Started

---

### 1. Clone the Repository
```bash
$ git clone https://github.com/FA24SE161-Cabonerf/cabonerf-be.git
$ cd cabonerf-be
```

### 2. Configure Database
1. Ensure PostgreSQL is running on your system.
2. Create a new database for the project.
```sql
CREATE DATABASE cabonerf;
```
3. Update `application.properties` or `application.yml` with your PostgreSQL credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cabonerf
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build the Project
Run the following command to build the project:
```bash
$ mvn clean install
```

### 4. Run the Application
Start the Spring Boot application:
```bash
$ mvn spring-boot:run
```

The application will start on `http://localhost:8080` by default.

# Additional Features

---

### 1. Swagger API Documentation
Swagger UI is enabled to explore the APIs visually. Once the application is running, navigate to:
```
http://localhost:8080/swagger-ui/index.html
```

# Troubleshooting

---

### Common Issues
1. **Database Connection Error**:
    - Ensure PostgreSQL is running.
    - Verify the database credentials in `application.properties`.
2. **Port Already in Use**:
    - Stop any process using port 8080 or change the server port in `application.properties`:
      ```properties
      server.port=8081
      ```

# Contributing

---

We welcome contributions to the Cabonerf Backend project. Please follow these steps:
1. Fork the repository.
2. Create a new feature branch.
3. Commit your changes with clear messages.
4. Submit a pull request.

# License

---

This project is licensed under the [License](LICENSE).

