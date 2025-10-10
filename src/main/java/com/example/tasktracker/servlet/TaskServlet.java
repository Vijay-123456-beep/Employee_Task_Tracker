package com.example.tasktracker.servlet;

import com.example.tasktracker.dao.TaskDAO;
import com.example.tasktracker.model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TaskServlet", urlPatterns = {"/api/tasks"})
public class TaskServlet extends HttpServlet {
    private transient TaskDAO dao;
    private transient Gson gson;

    @Override
    public void init() throws ServletException {
        this.dao = new TaskDAO();
        this.gson = new GsonBuilder().serializeNulls().create();
    }

    private void setCors(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            List<Task> tasks = dao.findAll();
            out.print(gson.toJson(tasks));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        resp.setContentType("application/json");
        try {
            Task t = readBodyAsTask(req);
            if (t.getStatus() == null || t.getStatus().isEmpty()) t.setStatus("TODO");
            Task created = dao.create(t);
            resp.getWriter().print(gson.toJson(created));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        resp.setContentType("application/json");
        try {
            Task t = readBodyAsTask(req);
            if (t.getId() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("{\"error\":\"Missing id for update\"}");
                return;
            }
            Task updated = dao.update(t);
            resp.getWriter().print(gson.toJson(updated));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        resp.setContentType("application/json");
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\":\"Missing id parameter\"}");
            return;
        }
        try {
            int id = Integer.parseInt(idParam);
            boolean ok = dao.delete(id);
            resp.getWriter().print("{\"deleted\":" + ok + "}");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    private Task readBodyAsTask(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = req.getReader()) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }
        return gson.fromJson(sb.toString(), Task.class);
    }
}
