# Accounting Service

This is a Spring Boot application built with Java 17 and Spring Boot 3.3.2. The application provides a RESTful API for managing accounts and transactions. It uses MySQL as the underlying database.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [Docker Support](#docker-support)
- [Swagger API Documentation URL](#swagger-api-documentation-url)
- [Unit Test Coverage Report](#unit-test-coverage-report)

## Features

- Create and manage accounts.
- Perform transactions on accounts (e.g., purchase, withdrawal, credit voucher).
- RESTful API design with JSON responses.
- Integration with MySQL database.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java 17 installed.
- Maven 3.9.8 installed.
- MySQL 9.0.1 installed and running.
- Docker installed for containerized deployment.

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/accounting-service.git
cd accounting-service
```

## Running the Application
##### To run from terminal use command   ```./mvnw spring-boot:run```

or

#### Use Command ```./run.sh``` to run application as docker container

## Docker Support

This guide details how to set up and run the accounting service application alongside a MySQL database using Docker on your local machine.

### Prerequisites:

#### Docker: Ensure you have Docker installed on your system. Refer to the official Docker documentation for installation instructions: https://docs.docker.com/engine/install/

##### Steps:

Navigate to the project directory:
Open a terminal window and navigate to the directory containing the run.sh script.
For example:
```cd accounting-service```

##### Run the application:
Execute the following command to start both the accounting service application and the MySQL database within a Docker container:

```./run.sh```

This script likely handles setting the necessary environment variables and executing the appropriate Docker commands.


## Swagger API Documentation URL
http://localhost:8080/swagger-ui.html


## Unit Test Coverage Report
Run below-mentioned command to generate unit test coverage report.
After running command go to target/site folder and open index.html file to see report
#### Jacoco test report terminal command : ```./mvnw clean test jacoco:report```

