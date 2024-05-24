# Java Spring Boot MongoDB Example

## Overview
This project is a simple Java Spring Boot application that integrates with MongoDB. It provides a REST API to manage a "Hello World" message, allowing CRUD operations such as fetching, creating, and updating the message.

## Functionality
The application includes:
- **GET `/hello`**: Fetches the "Hello World" message from MongoDB. If the message does not exist, it returns a default message.
- **POST `/hello`**: Creates a new "Hello World" message in MongoDB or updates the existing one if it already exists.
- **PUT `/hello`**: Updates the existing "Hello World" message in MongoDB.

## Technologies
- **Java**: The application is written in Java, using Spring Boot as the framework.
- **Spring Data MongoDB**: Manages interactions with MongoDB for CRUD operations.
- **Maven**: Used for project management and build automation.
- **Docker**: The application have to be containerized using Docker.

## Usage for Educational Purposes
This application is designed to be used as a learning tool for understanding how to containerize such applications with Docker.
