package com.example.todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TodoService - Service class for managing todo operations
 */
public class TodoService {
    private final List<Todo> todos;
    private final AtomicInteger idCounter;

    public TodoService() {
        this.todos = new ArrayList<>();
        this.idCounter = new AtomicInteger(1);
    }

    /**
     * Get all todos
     * @return List of all todos
     */
    public List<Todo> getAllTodos() {
        return new ArrayList<>(todos);
    }

    /**
     * Get a todo by ID
     * @param id The todo ID
     * @return Optional containing the todo if found
     */
    public Optional<Todo> getTodoById(int id) {
        return todos.stream()
                   .filter(todo -> todo.getId() == id)
                   .findFirst();
    }

    /**
     * Add a new todo
     * @param title The todo title
     * @return The created todo
     */
    public Todo addTodo(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Todo title cannot be null or empty");
        }
        
        Todo todo = new Todo(idCounter.getAndIncrement(), title.trim());
        todos.add(todo);
        return todo;
    }

    /**
     * Complete a todo by ID
     * @param id The todo ID
     * @return true if the todo was found and completed, false otherwise
     */
    public boolean completeTodo(int id) {
        Optional<Todo> todoOpt = getTodoById(id);
        if (todoOpt.isPresent()) {
            todoOpt.get().setComplete(true);
            return true;
        }
        return false;
    }

    /**
     * Toggle the completion status of a todo
     * @param id The todo ID
     * @return true if the todo was found and toggled, false otherwise
     */
    public boolean toggleTodo(int id) {
        Optional<Todo> todoOpt = getTodoById(id);
        if (todoOpt.isPresent()) {
            todoOpt.get().toggleComplete();
            return true;
        }
        return false;
    }

    /**
     * Delete a todo by ID
     * @param id The todo ID
     * @return true if the todo was found and deleted, false otherwise
     */
    public boolean deleteTodo(int id) {
        return todos.removeIf(todo -> todo.getId() == id);
    }

    /**
     * Update a todo's title
     * @param id The todo ID
     * @param newTitle The new title
     * @return true if the todo was found and updated, false otherwise
     */
    public boolean updateTodoTitle(int id, String newTitle) {
        if (newTitle == null || newTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Todo title cannot be null or empty");
        }
        
        Optional<Todo> todoOpt = getTodoById(id);
        if (todoOpt.isPresent()) {
            todoOpt.get().setTitle(newTitle.trim());
            return true;
        }
        return false;
    }

    /**
     * Get the count of todos
     * @return Total number of todos
     */
    public int getTodoCount() {
        return todos.size();
    }

    /**
     * Get the count of completed todos
     * @return Number of completed todos
     */
    public int getCompletedCount() {
        return (int) todos.stream().filter(Todo::isComplete).count();
    }

    /**
     * Get the count of pending todos
     * @return Number of pending todos
     */
    public int getPendingCount() {
        return getTodoCount() - getCompletedCount();
    }

    /**
     * Clear all todos
     */
    public void clearAllTodos() {
        todos.clear();
        idCounter.set(1);
    }
} 