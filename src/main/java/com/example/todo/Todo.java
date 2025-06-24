package com.example.todo;

import java.util.Objects;

/**
 * Todo - Represents a single todo item
 */
public class Todo {
    private int id;
    private String title;
    private boolean complete;

    public Todo() {
    }

    public Todo(int id, String title) {
        this.id = id;
        this.title = title;
        this.complete = false;
    }

    public Todo(int id, String title, boolean complete) {
        this.id = id;
        this.title = title;
        this.complete = complete;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    // Helper methods
    public void toggleComplete() {
        this.complete = !this.complete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return id == todo.id && 
               complete == todo.complete && 
               Objects.equals(title, todo.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, complete);
    }

    @Override
    public String toString() {
        return String.format("Todo{id=%d, title='%s', complete=%s}", id, title, complete);
    }
} 