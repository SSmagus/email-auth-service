# Email Authentication Service  
A Spring Boot backend service providing email-based authentication with verification links, secure JWT login, and password reset flows. Designed as a lightweight, production-ready authentication module for any backend application.

## Features
- Email verification using unique, time-bound tokens  
- JWT-based login with BCrypt password hashing  
- Password reset flow with secure reset links  
- Fast email delivery using optimized SMTP configurations  
- Stateless authentication using Spring Security  
- Clean REST API tested through Postman  

## Tech Stack
- Java 17  
- Spring Boot 3  
- Spring Security  
- Spring Data JPA  
- MySQL  
- JavaMailSender  
- JWT (jjwt)  
- BCrypt hashing  

## API Endpoints

### Register
POST /api/auth/register
{
  "email": "user@example.com",
  "password": "password123"
}

### Verify Email
GET /api/auth/verify?token=abc123

### Login
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "password123"
}

### Request Password Reset
POST /api/auth/request-reset
{
  "email": "user@example.com"
}

### Reset Password
POST /api/auth/reset
{
  "token": "abc123",
  "newPassword": "newPass123"
}

## How It Works
1. User registers → service sends a verification email containing a unique token  
2. User clicks the link → account is marked as verified  
3. User logs in → receives a signed JWT token  
4. Forgot password → service emails a secure reset link  
5. User resets password using a time-bound token  

## Environment Configuration
Set MySQL and email SMTP credentials inside application.yml:

spring.datasource.username=
spring.datasource.password=

spring.mail.username=
spring.mail.password=

app.jwt.secret=

## Running the Project
mvn spring-boot:run

## Folder Structure
src/main/java/com/saumya/auth/
  config/
  controller/
  dto/
  entity/
  repository/
  service/

## License
MIT License
