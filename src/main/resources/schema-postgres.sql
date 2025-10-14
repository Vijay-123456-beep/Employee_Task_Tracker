-- PostgreSQL Schema for Render Deployment
-- This will be automatically run by Render

CREATE TABLE IF NOT EXISTS tasks (
  id SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  assignee VARCHAR(120) NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'TODO',
  due_date DATE NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
