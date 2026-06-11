# Lifestyle Tracker

A Spring Boot REST API for tracking fitness data: training sessions, daily habits, weight logs, and food intake. Includes optional Garmin Connect integration to sync activity and health data.

## Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose (for the recommended setup)

---

## Quick Start (Docker)

This is the easiest way to get everything running — PostgreSQL and the app start together.

**1. Clone the repo**

```bash
git clone <repo-url>
cd lifestyle-tracker
```

**2. Create your `.env` file**

```bash
cp .env.example .env
```

Open `.env` and fill in your values:

```env
DB_NAME=fitnessdb
DB_USERNAME=fitness
DB_PASSWORD=changeme

# Only needed if you want Garmin Connect integration
GARMIN_EMAIL=your-garmin-email@example.com
GARMIN_PASSWORD=your-garmin-password
```

**3. Start the stack**

```bash
docker compose up --build
```

The app will be available at **http://localhost:8080**.

---

## Applying Frontend or Backend Changes (Docker)

Static files and Java code are baked into the Docker image at build time. After any code change, rebuild and restart only the app container:

```bash
docker compose up --build app -d
```

Then hard-refresh the browser (`Cmd+Shift+R` on Mac, `Ctrl+Shift+R` on Windows/Linux). The `postgres` and `garmin` containers are unaffected.

---

## Local Development (without Docker)

Use this if you want to run the Spring Boot app directly with Maven.

**1. Start a PostgreSQL instance**

You can use Docker just for the database:

```bash
docker compose up postgres
```

Or connect to any existing PostgreSQL instance by setting the env vars below.

**2. Set environment variables**

Export the variables or add them to your shell profile:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=fitnessdb
export DB_USERNAME=fitness
export DB_PASSWORD=changeme

# Optional — Garmin integration
export GARMIN_EMAIL=your-garmin-email@example.com
export GARMIN_PASSWORD=your-garmin-password
```

**3. Run the app**

```bash
mvn spring-boot:run
```

---

## Available Endpoints

| URL | Description |
|-----|-------------|
| http://localhost:8080 | Web UI |
| http://localhost:8080/swagger-ui.html | Swagger / interactive API docs |
| http://localhost:8080/api-docs | OpenAPI JSON spec |

---

## Seed Data

On first startup (empty database), the app seeds a demo user with sample habits and upcoming training sessions. To reset, delete the database volume and restart:

```bash
docker compose down -v
docker compose up
```

---

## Build

```bash
# Compile and package
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Run tests
mvn test
```

---

## Project Structure

```
src/main/java/com/fitness/
├── config/        # DataInitializer, GlobalExceptionHandler
├── controller/    # REST controllers
├── dto/           # Request/response DTOs
├── model/         # JPA entities and enums
├── repository/    # Spring Data JPA repositories
└── service/       # Business logic

src/main/resources/
├── application.properties   # App config (env-var substitution)
└── static/                  # Frontend HTML pages

garmin_service.py            # Python sidecar for Garmin Connect API
docker-compose.yml           # Full-stack Docker setup
Dockerfile                   # Multi-stage build (Maven + JRE + Python)
```

---

## Garmin Integration

The Garmin sidecar is a small Flask service (`garmin_service.py`) that proxies requests to the Garmin Connect API. It runs alongside the Spring Boot app inside Docker.

Set `GARMIN_EMAIL` and `GARMIN_PASSWORD` in your `.env` file to enable it. OAuth tokens are cached in a Docker volume (`garmin_tokens`) so you only authenticate once.

Endpoints proxied by the sidecar (accessible at port 5000 internally):

- `GET /garmin/activities`
- `GET /garmin/stats`
- `GET /garmin/sleep`
- `GET /garmin/heartrate`
