# Expense Sharing Application

This is a Spring Boot application for managing shared expenses among users. It allows users to add expenses, split them in various ways, and view balance sheets.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Getting Started](#getting-started)
3. [Running the Application](#running-the-application)
4. [API Documentation](#api-documentation)
5. [Features](#features)
6. [Security](#security)
7. [Testing](#testing)
8. [Contributing](#contributing)

## Prerequisites

- Java JDK 11 or later
- Gradle 6.8 or later
- MySQL 5.7 or later

## Getting Started

1. Clone the repository:

2. Configure the database:
- Create a MySQL database named `expenses_sharing_db`
- Update `src/main/resources/application.properties` with your MySQL username and password:
  ```
  spring.datasource.username=your_username
  spring.datasource.password=your_password
  ```

## Running the Application

To run the application, use the following command in the project root directory:

The application will start on `http://localhost:8080`.

## API Documentation

Swagger UI is integrated for API documentation and testing. Once the application is running, you can access it at:

The OpenAPI specification is available at:

## Features

- User Management: Create and retrieve user details
- Expense Management: Add expenses with various splitting methods (Equal, Exact, Percentage)
- Balance Sheet: Generate and download balance sheets
- API Documentation: Explore and test API endpoints using Swagger UI

## Security

For development and testing purposes, security checks are currently disabled. Remember to implement proper security measures before deploying to a production environment.

## Testing

To run the tests, use the following command:

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

For any issues or questions, please open an issue in the GitHub repository.