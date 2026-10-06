# FoodBridge

A full-stack surplus food donation platform built with:

- Java 17
- Spring Boot
- Spring Web / REST
- Spring Data JPA + Hibernate
- PostgreSQL
- HTML/CSS/JavaScript frontend
- BCrypt password hashing
- Simple role-based authentication flow

## Features

- Landing page
- Login / registration
- Donor, NGO and volunteer roles
- Create surplus food donations
- Browse available donations
- Claim donations
- Dashboard statistics
- REST API
- PostgreSQL-ready persistence

## Run

### 1. Database

Create a PostgreSQL database:

```sql
CREATE DATABASE foodbridge;
```

### 2. Configure

Edit:

`src/main/resources/application.properties`

Set your PostgreSQL username/password.

### 3. Start

```bash
mvn spring-boot:run
```

Open:

http://localhost:8080/

## API

```text
POST /api/auth/register
POST /api/auth/login
GET  /api/donations
POST /api/donations
POST /api/donations/{id}/claim
GET  /api/stats
```

For a hackathon prototype, authentication uses a simple server-side session token. For production, replace it with JWT/OAuth2 and add proper authorization policies, email verification, rate limiting and audit logging.
