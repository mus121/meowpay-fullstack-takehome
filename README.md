# SadaPay

A full-stack application with a Kotlin/Spring Boot backend and a Next.js frontend for managing cat transfers.

## Project Structure

- backend/: Spring Boot API with Kotlin
- frontend/: Next.js web app
- docker-compose.yml: Container setup for the app

## Prerequisites

- Java 17+
- Node.js 18+
- Docker and Docker Compose (optional, for containerized setup)

## Backend

Navigate to the backend folder and run:

```bash
./gradlew bootRun
```

The backend runs with the development configuration by default.

## Frontend

Navigate to the frontend folder and run:

```bash
npm install
npm run dev
```

Then open http://localhost:3000 in your browser.

## Docker

To run the app with Docker Compose:

```bash
docker-compose up --build
```

## Notes

This project includes:
- REST APIs for cat and transfer management
- React/Next.js UI
- Tests for backend services and integration flows
