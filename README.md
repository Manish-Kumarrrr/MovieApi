# MovieApi

**MovieApi** is a backend application built using **Spring Boot** that provides a RESTful API for managing movie-related data. The application is designed with **role-based authentication** using **JWT** (JSON Web Tokens) and **Spring Security**, ensuring secure access to different API endpoints. It uses **MySQL** as the database and **Redis** for caching to enhance performance. This API supports pagination, sorting, and secure user authentication, including password resets.

---

## Features

- **RESTful Architecture**: The API follows the principles of REST, ensuring scalability and ease of integration.
- **Role-Based Authentication**: Secure endpoints using **JWT** and **Spring Security** to manage user access levels (e.g., Admin, User).
- **Caching with Redis**: Use of Redis for caching frequently accessed movie data, providing faster response times.
- **Pagination and Sorting**: Optimized data retrieval with pagination and sorting for large datasets.
- **Secure Email Authentication**: Integration of email-based authentication for secure password reset functionality.

---

## Tech Stack

- **Backend**: 
  - [Spring Boot](https://spring.io/projects/spring-boot) - Java-based framework for building the backend.
  - [Spring Security](https://spring.io/projects/spring-security) - Used for securing endpoints with JWT-based authentication and authorization.
  - [JWT (JSON Web Tokens)](https://jwt.io/) - Secure and stateless authentication mechanism for managing user sessions.
  - [MySQL](https://www.mysql.com/) - Relational database management system to store movie-related data and user information.
  - [Redis](https://redis.io/) - In-memory data store used for caching frequently requested data to improve performance.

---

## Installation

### Prerequisites

- **JDK 11 or higher**
- **Maven** (for building the project)
- **MySQL Database** (for storing movie and user data)
- **Redis** (for caching purposes)

### Setup Instructions

1. **Clone the repository**:

   ```bash
   https://github.com/Manish-Kumarrrr/MovieApi.git
2. **Set up the database:**:

      Create a MySQL database called movieapi (or use an existing one).
      Import the provided SQL schema or run migrations (if any).
      Configure application properties:
      
      Open src/main/resources/application.properties and configure the following:
      
      MySQL Database Configuration:
      
      properties

   ```bash
       spring.datasource.url=jdbc:mysql://localhost:3306/movieapi
      spring.datasource.username=your-username
      spring.datasource.password=your-password
      spring.jpa.hibernate.ddl-auto=update
