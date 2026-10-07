# Project Structure - Easy Explanation

## 1. controller
Example: `PatientController`

The controller is like the entry point of the API.
It receives requests such as:

`GET /api/patients`

and calls the service.

## 2. service
Example: `PatientService`

Business logic belongs here. The controller should stay small.

## 3. repository
Example: `PatientRepository`

Spring Data JPA talks to MySQL through repository interfaces.

## 4. entity
Example: `Patient`

`@Entity` tells JPA that the Java class represents a database table.

## 5. exception
`@RestControllerAdvice` gives one common place for API errors.

## 6. security
JWT login/authentication code is kept separately so the main business classes remain readable.

## Typical flow

```text
React/Postman
     |
     v
PatientController
     |
     v
PatientService
     |
     v
PatientRepository
     |
     v
MySQL (patients table)
```
