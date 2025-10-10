# Employee Task Tracker (AngularJS + Java Servlets + MySQL)

A minimal, runnable CRUD app to assign and track employee tasks.

## Tech
- Frontend: AngularJS 1.8, Bootstrap 5 (static under `src/main/webapp/`)
- Backend: Java Servlets (WAR), Gson, JDBC
- DB: MySQL
- Dev Server: Jetty (`mvn jetty:run`)

## Prerequisites
- JDK 11+
- Maven 3.8+
- MySQL 8+

## Setup DB
1. Start MySQL and run the schema:
   ```sql
   SOURCE src/main/resources/schema.sql;
   ```
2. Connection defaults (override via environment variables):
   - `DB_URL` (default `jdbc:mysql://localhost:3306/employee_task_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`)
   - `DB_USER` (default `root`)
   - `DB_PASS` (default empty)

## Run
```bash
mvn clean package
mvn jetty:run
```
Then open: http://localhost:8080/

The app calls REST endpoints at `GET/POST/PUT/DELETE /api/tasks`.

## API
- `GET /api/tasks` → list tasks
- `POST /api/tasks` → create task (JSON body)
- `PUT /api/tasks` → update task (JSON body with `id`)
- `DELETE /api/tasks?id=ID` → delete task

Body example:
```json
{
  "title": "Prepare report",
  "description": "Q3 performance",
  "assignee": "Anita",
  "status": "TODO",
  "dueDate": "2025-10-31"
}
```

## Notes
- CORS headers are enabled in `TaskServlet` to simplify cross-origin during dev.
- For production, secure DB creds and harden CORS.
