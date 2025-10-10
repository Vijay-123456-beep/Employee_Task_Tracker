package com.example.tasktracker.dao;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public List<Task> findAll() throws SQLException {
        String sql = "SELECT id, title, description, assignee, status, due_date, created_at FROM tasks ORDER BY id DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Task> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public Task findById(int id) throws SQLException {
        String sql = "SELECT id, title, description, assignee, status, due_date, created_at FROM tasks WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        }
    }

    public Task create(Task t) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, assignee, status, due_date) VALUES (?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getTitle());
            ps.setString(2, t.getDescription());
            ps.setString(3, t.getAssignee());
            ps.setString(4, t.getStatus());
            if (t.getDueDate() != null && !t.getDueDate().isEmpty()) {
                ps.setDate(5, Date.valueOf(t.getDueDate()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return findById(id);
                }
            }
        }
        return null;
    }

    public Task update(Task t) throws SQLException {
        String sql = "UPDATE tasks SET title=?, description=?, assignee=?, status=?, due_date=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTitle());
            ps.setString(2, t.getDescription());
            ps.setString(3, t.getAssignee());
            ps.setString(4, t.getStatus());
            if (t.getDueDate() != null && !t.getDueDate().isEmpty()) {
                ps.setDate(5, Date.valueOf(t.getDueDate()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.setInt(6, t.getId());
            ps.executeUpdate();
            return findById(t.getId());
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Task mapRow(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        String assignee = rs.getString("assignee");
        String status = rs.getString("status");
        Date due = rs.getDate("due_date");
        Timestamp created = rs.getTimestamp("created_at");
        String dueStr = (due != null) ? due.toLocalDate().toString() : null;
        String createdStr = (created != null) ? created.toInstant().toString() : null;
        return new Task(id, title, description, assignee, status, dueStr, createdStr);
    }
}
