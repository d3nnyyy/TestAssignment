# User Management System - Test Assignment

This is a simple user management system implemented using Spring Boot and Java. It provides RESTful endpoints for managing user data, such as creating, updating, retrieving, and deleting user records.

## Table of Contents

- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
  - [Endpoints](#endpoints)
- [Testing](#testing)

## Getting Started

### Prerequisites

To run this application, you will need the following prerequisites:

- Java Development Kit (JDK) 11 or later
- Apache Maven (for building the application)
- An Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse (optional)

### Installation

1. Clone this repository to your local machine:

   ```bash
   git clone https://github.com/d3nnyyy/TestAssignment
   ```
   

2. Open the project in your IDE or navigate to the project directory in the terminal.
3. Build the project using Maven:

  ```
  mvn clean install
  ```

4. Run the application:

  ```
  mvn spring-boot:run
  ```

The application should now be running locally on port 8080.

## Usage

### Endpoints

The application provides the following RESTful endpoints for managing user data:

- GET /api/users: Get a list of all users.

- GET /api/users/{id}: Get a user by their ID.

- GET /api/users/birthdays: Get users with birthdays within a specified date range.

- POST /api/users: Create a new user.

- PUT /api/users/{id}: Update an existing user by their ID.

- DELETE /api/users/{id}: Delete a user by their ID.

Here are some sample requests you can make using a tool like curl or a REST client like Postman:

#### Get All Users

```
curl http://localhost:8080/api/users
```

#### Get User by ID

```
curl http://localhost:8080/api/users/{id}
```

#### Create User

```
curl -X POST -H "Content-Type: application/json" -d '{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "dateOfBirth": "1990-01-01",
  "address": "New York",
  "phoneNumber": "+1234567890"
}' http://localhost:8080/api/users
```

#### Update User

```
curl -X PUT -H "Content-Type: application/json" -d '{
  "firstName": "Updated",
  "lastName": "User",
  "email": "updated.user@example.com",
  "dateOfBirth": "1985-01-01",
  "address": "Updated Address",
  "phoneNumber": "+9876543210"
}' http://localhost:8080/api/users/{id}
```

#### Delete User

```
curl -X DELETE http://localhost:8080/api/users/{id}
```

Please note that you should replace {id} with the actual user ID when making requests.

## Testing

The application includes unit tests to ensure its functionality. You can run the tests using the following Maven command:

```
mvn test
```
