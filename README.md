Job Tracker Backend

Overview
A Spring Boot backend for a job tracker app. Features: user registration, JWT access+refresh tokens, validation, global error handling, Flyway migrations, Docker, and CI.

Prerequisites
- Java 21
- Maven
- Docker (optional)

Run locally
- Build: mvn -DskipTests package
- Run: java -jar target/job-tracker-backend-0.0.1-SNAPSHOT.jar
- Tests: mvn test

Environment (overwrite defaults in application.yaml)
- APP_JWT_SECRET or app.jwt.secret in application.yml (change from default)
- SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD

Docker
- docker compose up --build

Endpoints
- POST /api/auth/register
  JSON: {"pseudo":"alice","email":"a@a.com","password":"password123"}
- POST /api/auth/login
  JSON: {"email":"a@a.com","password":"password123"}
  Response: {"accessToken":"...","refreshToken":"..."}
- POST /api/auth/refresh
  JSON: {"refreshToken":"..."}
  Response: {"accessToken":"..."}
- POST /api/auth/logout
  JSON: {"refreshToken":"..."}

cURL examples
Register:
  curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"pseudo":"anna","email":"anna@example.com","password":"password123"}'
Login:
  curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"email":"anna@example.com","password":"password123"}'
Refresh:
  curl -X POST http://localhost:8080/api/auth/refresh -H "Content-Type: application/json" -d '{"refreshToken":"<token>"}'
Logout:
  curl -X POST http://localhost:8080/api/auth/logout -H "Content-Type: application/json" -d '{"refreshToken":"<token>"}'

CI
- GitHub Actions workflow included: .github/workflows/ci.yml runs mvn test on push/pull_request.

Notes
- Replace default JWT secret before publishing.
- Flyway migrations are in src/main/resources/db/migration.
- Tests use H2 in-memory DB (configured in test properties).

Next steps
Add password reset, role-based auth, better logging, and e2e integration tests.
