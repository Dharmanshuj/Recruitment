# Zinios Onboard Service

This is a Spring Boot microservice for the Zinios Onboard application with integrated JWT authentication and security.

## Technology Stack
- **Java**: 21
- **Spring Boot**: 3.x
- **Spring Security**: Integrated JWT authentication
- **Maven**: Latest
- **Database**: MySQL 8.0+

## Features
- RESTful APIs with Swagger documentation
- Integrated JWT token validation and Spring Security
- API whitelisting and authentication
- Global exception handling
- MySQL database integration with JPA
- Environment-wise configuration (dev, QA, stage, prod)
- JUnit test cases
- Lombok for boilerplate code reduction

## Project Structure

```
zinios-onboard-service/
├── src/main/java/com/zinios/onboard/
│   ├── ZiniosOnboardServiceApplication.java
│   ├── controller/
│   │   ├── HealthController.java
│   │   └── AuthController.java
│   ├── service/
│   │   └── JwtService.java
│   ├── filter/
│   │   └── JwtAuthenticationFilter.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ErrorResponse.java
│   │   └── ZiniosException.java
│   └── config/
│       ├── SecurityConfig.java
│       └── JpaConfig.java
├── src/main/resources/
│   ├── application.properties
│   ├── application-dev.properties
│   ├── application-qa.properties
│   ├── application-stage.properties
│   └── application-prod.properties
└── src/test/
```

## Database Configuration

### Environment-wise Database Setup
- **Development**: `zinios_onboard_dev`
- **QA**: `zinios_onboard_qa`
- **Stage**: `zinios_onboard_stage`
- **Production**: `zinios_onboard_prod`

All environments use MySQL with:
- URL: `localhost:3306`
- Username: `root`
- Password: `root`

## Security Features

### JWT Authentication
- Token validation on every request (except whitelisted URLs)
- Configurable JWT secret and expiration
- Token generation and validation services

### Whitelisted URLs
The following URLs don't require authentication:
- `/api/auth/**` - Authentication endpoints
- `/api/health` - Health check
- `/actuator/**` - Actuator endpoints
- `/swagger-ui/**` - Swagger UI
- `/v3/api-docs/**` - OpenAPI docs

### Spring Security Integration
- Custom JWT authentication filter
- Stateless session management
- BCrypt password encoder
- Proper error handling for authentication failures

## How to Run

### Prerequisites
- Java 21
- Maven 3.8+
- MySQL 8.0+

### Steps

1. **Setup Database**
   ```sql
   CREATE DATABASE zinios_onboard_dev;
   CREATE DATABASE zinios_onboard_qa;
   CREATE DATABASE zinios_onboard_stage;
   CREATE DATABASE zinios_onboard_prod;
   ```

2. **Run the Service**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

3. **Access APIs**
   - Main Service: http://localhost:8082
   - Swagger UI: http://localhost:8082/swagger-ui.html
   - Health Check: http://localhost:8082/api/health

## API Endpoints

### Authentication
- `POST /api/auth/login` - Login with credentials
- `POST /api/auth/register` - Register new user

### Health
- `GET /api/health` - Health check

### Test Login
Use the following credentials for testing:
- Username: `admin`
- Password: `password`

## Testing

Run tests with:
```bash
mvn test
```

## Configuration

### JPA Configuration
- Repository scanning enabled for `com.zinios.onboard.repository`
- JPA auditing enabled for automatic timestamp management
- Transaction management enabled

### JWT Configuration
- Secret key: Base64 encoded (configurable)
- Token expiration: 24 hours (configurable)
- Secure token parsing with HMAC SHA algorithms

### Global Exception Handler
- Custom error responses with validation details
- Proper HTTP status codes
- Structured error format with timestamps

## API Documentation

Swagger UI is available at: http://localhost:8082/swagger-ui.html

## Port Configuration
- Service runs on port **8082** (different from wellness-pro which runs on 8080)