package com.example.tasktracker.model;

public class Task {
    private Integer id;
    private String title;
    private String description;
    private String assignee;
    private String status; // e.g., TODO, IN_PROGRESS, DONE
    private String dueDate; // ISO yyyy-MM-dd
    private String createdAt; // ISO timestamp string

    public Task() {}

    public Task(Integer id, String title, String description, String assignee, String status, String dueDate, String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
