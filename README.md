# Customer Notification Service

## Introduction

[cite_start]This project is a robust, full-featured microservice designed to centralize and manage customer contact information, notification preferences, and delivery statuses. [cite_start]It serves as a single source of truth for all customer communication data, providing a secure and efficient RESTful API for other systems in the ecosystem.

The application is built using a modern Java technology stack, emphasizing a clean, maintainable, and scalable architecture.

## Features

The service successfully implements all the key functional requirements outlined in the project specification:

-   **Admin User Management**:
    -   [cite_start][x] Secure, role-based authentication for administrators using JWT. 
    -   [cite_start][x] Endpoints for creating and managing admin accounts. 
-   **Full Customer & Data Lifecycle Management**:
    -   [cite_start][x] Complete CRUD (Create, Read, Update, Delete) operations for Customers, Addresses, Preferences, and Notifications. 
    -   [cite_start][x] Support for multiple address types (Email, SMS, Postal). 
    -   [cite_start][x] Manages customer notification opt-in/opt-out preferences per channel. 
-   **Advanced Functionality**:
    -   [cite_start][x] High-performance batch updates for customer records. 
    -   [cite_start][x] Dynamic, criteria-based customer search and filtering. 
    -   [cite_start][x] Paginated and sortable lists for all major entities. 
-   **Reporting & Analytics**:
    -   [cite_start][x] API endpoints to generate real-time statistics on notification statuses (e.g., PENDING, SENT, FAILED). 
    -   [cite_start][x] API endpoints to report on customer preference distribution by channel (e.g., number of customers opted-in for EMAIL). 
-   **Web Interface**:
    -   [cite_start][x] A clean, responsive, and easy-to-use web interface for administrators to manage all aspects of the system. 

## Technology Stack

The project leverages a modern and powerful technology stack, chosen for its robustness, performance, and wide adoption in the industry.

| Category              | Technology                                       |
| --------------------- | ------------------------------------------------ |
| **Backend** | Java 17, Spring Boot 3.1.2                       |
| **Data Persistence** | Spring Data JPA, Hibernate                       |
| **Database** | H2 (for development/testing), MySQL (supported)  |
| **Security** | Spring Security 6, JSON Web Tokens (JWT)         |
| **API Documentation** | SpringDoc (OpenAPI 3 / Swagger UI)               |
| **DTO Mapping** | MapStruct                                        |
| **Frontend** | Thymeleaf, HTML5, CSS3, JavaScript, Bootstrap 5  |
| **Build Tool** | Maven (with Maven Wrapper for portability)       |

## Architectural Design

[cite_start]The application is built upon a well-organized, layered architecture to ensure a clean separation of concerns, high maintainability, and scalability.

1.  **Controller Layer**: Handles all incoming HTTP requests. Its sole responsibility is to validate input, delegate business logic to the service layer, and format the response. It uses DTOs (Data Transfer Objects) to define its public API contract, keeping it separate from the internal data model.
2.  **Service Layer**: Contains the core business logic of the application. It orchestrates operations between the repositories and other services, handles transactions (`@Transactional`), and ensures data integrity.
3.  **Repository Layer**: An abstraction over the database using Spring Data JPA. It manages all data access and provides a clean, high-level interface for CRUD operations and custom queries.
4.  **Security Layer**: Integrated via Spring Security. A `JwtAuthenticationFilter` intercepts API requests to validate JWTs for stateless, secure authentication. User and role details are managed by a `UserDetailsServiceImpl`. Access control is enforced at both the endpoint level (`SecurityConfig`) and method level (`@PreAuthorize`).
5.  **Specification & DTOs**: Advanced search functionality is implemented using the JPA Specification pattern (`CustomerSpecification`), allowing for the dynamic, type-safe construction of complex queries. DTOs are used throughout to ensure a clean separation between the database entities and the API layer, with object mapping handled efficiently by MapStruct.
6.  [cite_start]**Exception Handling**: A global exception handler (`@RestControllerAdvice`) intercepts exceptions and transforms them into consistent, user-friendly JSON error responses, promoting graceful error handling.

## Testing Strategy

The project is thoroughly tested using a suite of **integration tests** that provide a high degree of confidence in the application's correctness and stability.

-   **Full-Stack Testing**: The tests use `@SpringBootTest` and `MockMvc` to launch the entire Spring application context and send real HTTP requests to the API endpoints. This validates every layer of the application, from the controllers down to the database.
-   **Comprehensive Coverage**: Tests cover the full CRUD lifecycle for all major entities, complex features like batch updates and advanced searching, and the reporting endpoints.
-   **End-to-End Validation**: The `UltimateTest.java` class provides a complete end-to-end test that simulates a complex user workflow, ensuring all parts of the system work together cohesively.
-   **Test Isolation**: Each test class is isolated from others using `@DirtiesContext` to guarantee that tests do not interfere with one another and that results are reliable and repeatable.

[cite_start]This robust testing strategy ensures that the application meets its functional requirements and is ready for deployment.

## Getting Started

Follow these instructions to get the application running on your local machine.

### Prerequisites

-   **Git**: To clone the repository.
-   **Java JDK 17**: The application is built with Java 17. Ensure your `JAVA_HOME` environment variable is correctly configured.

### Installation & Running

1.  **Clone the Repository**
    ```bash
    git clone <your-repository-url>
    cd customer-notification-service
    ```

2.  **Build the Application with the Maven Wrapper**
    The wrapper will automatically download the correct Maven version.
    * On **Linux** or **macOS**:
        ```bash
        # Make the script executable (first time only)
        chmod +x mvnw

        # Build the project
        ./mvnw clean install
        ```
    * On **Windows**:
        ```bash
        mvnw.cmd clean install
        ```

3.  **Run the Application**
    This command starts the embedded web server on port 8080.
    ```bash
    java -jar target/customer-notification-service-0.0.1-SNAPSHOT.jar
    ```

### Accessing the Application

-   **Web UI**: [http://localhost:8080/ui](http://localhost:8080/ui)
    -   Default admin credentials: `admin` / `admin123`
-   **API Documentation (Swagger)**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)