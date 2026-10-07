# HealthAI Hospital Backend

A Spring Starter Project style backend for the HealthAI Hospital Management System.

## Technology
- Java 17
- Spring Boot 4.1.1
- Maven
- Spring WebMVC
- Spring Data JPA
- MySQL
- Spring Security + JWT
- Validation
- Lombok
- DevTools
- Actuator

## Architecture

The code is intentionally divided into simple layers so it is easy to understand and explain in an interview:

```
controller  -> receives HTTP request / returns response
service     -> business logic
repository  -> database operations
entity      -> database tables / Java objects
dto         -> request objects
exception   -> common exception handling
config      -> application configuration / seed data
security    -> JWT authentication
```

## Main modules

- Authentication and registration
- Users and roles
- Patients
- Doctors and availability
- Departments
- Hospitals and nearest-hospital search
- Appointments and slots
- Prescriptions
- Medical records and file upload/download
- Invoices and payments
- Emergency / SOS cases
- Dashboard statistics
- Search
- Notifications
- Settings
- AI assistant endpoint

## Important URLs

```
GET  /api/health
POST /api/auth/login
POST /api/auth/register
GET  /api/patients
GET  /api/doctors
GET  /api/hospitals
GET  /api/departments
GET  /api/appointments
GET  /api/dashboard/stats
GET  /api/search?q=doctor
POST /api/ai/chat
```

## Run in STS

1. Extract this ZIP.
2. Open STS.
3. File -> Import -> Maven -> Existing Maven Projects.
4. Select the extracted project folder.
5. Finish.
6. Wait for Maven dependencies to download.
7. Right click `HealthAI-Hospital-Backend`.
8. Run As -> Spring Boot App.

## MySQL

Create/open MySQL and make sure the username/password in `application.properties` match your local MySQL installation.

The database name is `hospital_db`. With `createDatabaseIfNotExist=true`, MySQL can create it automatically when the configured user has permission.

## Error handling

`GlobalExceptionHandler` uses `@RestControllerAdvice` and handles:
- ResourceNotFoundException
- BadRequestException
- Validation errors
- Bad credentials
- Access denied
- Database constraint errors
- File size errors
- Invalid request data
- Unexpected errors

## Important

This backend is intentionally built as a Spring Starter Project rather than as a copied frontend/full-stack project. The React frontend can be connected later through the REST endpoints.
