package com.example.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

/**
 * Unit tests for the TodoService class
 */
@DisplayName("TodoService Tests")
public class TodoServiceTest {

    private TodoService todoService;

    @BeforeEach
    public void setUp() {
        todoService = new TodoService();
    }

    @Test
    @DisplayName("Should start with empty todo list")
    public void testInitialState() {
        assertThat(todoService.getAllTodos()).isEmpty();
        assertEquals(0, todoService.getTodoCount());
        assertEquals(0, todoService.getCompletedCount());
        assertEquals(0, todoService.getPendingCount());
    }

    @Test
    @DisplayName("Should add todo successfully")
    public void testAddTodo() {
        Todo todo = todoService.addTodo("Test Todo");
        
        assertNotNull(todo);
        assertEquals("Test Todo", todo.getTitle());
        assertFalse(todo.isComplete());
        assertEquals(1, todo.getId());
        assertEquals(1, todoService.getTodoCount());
    }

    @Test
    @DisplayName("Should throw exception when adding todo with null title")
    public void testAddTodoWithNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.addTodo(null);
        });
    }

    @Test
    @DisplayName("Should throw exception when adding todo with empty title")
    public void testAddTodoWithEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.addTodo("");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.addTodo("   ");
        });
    }

    @Test
    @DisplayName("Should trim whitespace from todo title")
    public void testAddTodoTrimsWhitespace() {
        Todo todo = todoService.addTodo("  Test Todo  ");
        
        assertEquals("Test Todo", todo.getTitle());
    }

    @Test
    @DisplayName("Should auto-increment todo IDs")
    public void testAutoIncrementIds() {
        Todo todo1 = todoService.addTodo("First Todo");
        Todo todo2 = todoService.addTodo("Second Todo");
        Todo todo3 = todoService.addTodo("Third Todo");
        
        assertEquals(1, todo1.getId());
        assertEquals(2, todo2.getId());
        assertEquals(3, todo3.getId());
    }

    @Test
    @DisplayName("Should get all todos")
    public void testGetAllTodos() {
        todoService.addTodo("First Todo");
        todoService.addTodo("Second Todo");
        
        List<Todo> todos = todoService.getAllTodos();
        
        assertEquals(2, todos.size());
        assertEquals("First Todo", todos.get(0).getTitle());
        assertEquals("Second Todo", todos.get(1).getTitle());
    }

    @Test
    @DisplayName("Should return defensive copy of todos list")
    public void testGetAllTodosReturnsDefensiveCopy() {
        todoService.addTodo("Test Todo");
        
        List<Todo> todos1 = todoService.getAllTodos();
        List<Todo> todos2 = todoService.getAllTodos();
        
        // Different instances
        assertNotSame(todos1, todos2);
        
        // Modifying returned list shouldn't affect service
        todos1.clear();
        assertEquals(1, todoService.getTodoCount());
    }

    @Test
    @DisplayName("Should get todo by ID")
    public void testGetTodoById() {
        Todo addedTodo = todoService.addTodo("Test Todo");
        
        Optional<Todo> foundTodo = todoService.getTodoById(addedTodo.getId());
        
        assertTrue(foundTodo.isPresent());
        assertEquals(addedTodo, foundTodo.get());
    }

    @Test
    @DisplayName("Should return empty optional for non-existent ID")
    public void testGetTodoByIdNotFound() {
        Optional<Todo> foundTodo = todoService.getTodoById(999);
        
        assertFalse(foundTodo.isPresent());
    }

    @Test
    @DisplayName("Should complete todo successfully")
    public void testCompleteTodo() {
        Todo todo = todoService.addTodo("Test Todo");
        
        boolean result = todoService.completeTodo(todo.getId());
        
        assertTrue(result);
        assertTrue(todo.isComplete());
        assertEquals(1, todoService.getCompletedCount());
        assertEquals(0, todoService.getPendingCount());
    }

    @Test
    @DisplayName("Should return false when completing non-existent todo")
    public void testCompleteTodoNotFound() {
        boolean result = todoService.completeTodo(999);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Should toggle todo successfully")
    public void testToggleTodo() {
        Todo todo = todoService.addTodo("Test Todo");
        
        // Toggle to complete
        boolean result1 = todoService.toggleTodo(todo.getId());
        assertTrue(result1);
        assertTrue(todo.isComplete());
        
        // Toggle back to incomplete
        boolean result2 = todoService.toggleTodo(todo.getId());
        assertTrue(result2);
        assertFalse(todo.isComplete());
    }

    @Test
    @DisplayName("Should return false when toggling non-existent todo")
    public void testToggleTodoNotFound() {
        boolean result = todoService.toggleTodo(999);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Should delete todo successfully")
    public void testDeleteTodo() {
        Todo todo = todoService.addTodo("Test Todo");
        
        boolean result = todoService.deleteTodo(todo.getId());
        
        assertTrue(result);
        assertEquals(0, todoService.getTodoCount());
        assertFalse(todoService.getTodoById(todo.getId()).isPresent());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent todo")
    public void testDeleteTodoNotFound() {
        boolean result = todoService.deleteTodo(999);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Should update todo title successfully")
    public void testUpdateTodoTitle() {
        Todo todo = todoService.addTodo("Original Title");
        
        boolean result = todoService.updateTodoTitle(todo.getId(), "Updated Title");
        
        assertTrue(result);
        assertEquals("Updated Title", todo.getTitle());
    }

    @Test
    @DisplayName("Should trim whitespace when updating todo title")
    public void testUpdateTodoTitleTrimsWhitespace() {
        Todo todo = todoService.addTodo("Original Title");
        
        todoService.updateTodoTitle(todo.getId(), "  Updated Title  ");
        
        assertEquals("Updated Title", todo.getTitle());
    }

    @Test
    @DisplayName("Should throw exception when updating with null title")
    public void testUpdateTodoTitleWithNull() {
        Todo todo = todoService.addTodo("Original Title");
        
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.updateTodoTitle(todo.getId(), null);
        });
    }

    @Test
    @DisplayName("Should throw exception when updating with empty title")
    public void testUpdateTodoTitleWithEmpty() {
        Todo todo = todoService.addTodo("Original Title");
        
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.updateTodoTitle(todo.getId(), "");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.updateTodoTitle(todo.getId(), "   ");
        });
    }

    @Test
    @DisplayName("Should return false when updating non-existent todo title")
    public void testUpdateTodoTitleNotFound() {
        boolean result = todoService.updateTodoTitle(999, "New Title");
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Should count todos correctly")
    public void testGetTodoCount() {
        assertEquals(0, todoService.getTodoCount());
        
        todoService.addTodo("First Todo");
        assertEquals(1, todoService.getTodoCount());
        
        todoService.addTodo("Second Todo");
        assertEquals(2, todoService.getTodoCount());
        
        todoService.deleteTodo(1);
        assertEquals(1, todoService.getTodoCount());
    }

    @Test
    @DisplayName("Should count completed todos correctly")
    public void testGetCompletedCount() {
        Todo todo1 = todoService.addTodo("First Todo");
        Todo todo2 = todoService.addTodo("Second Todo");
        Todo todo3 = todoService.addTodo("Third Todo");
        
        assertEquals(0, todoService.getCompletedCount());
        
        todoService.completeTodo(todo1.getId());
        assertEquals(1, todoService.getCompletedCount());
        
        todoService.completeTodo(todo2.getId());
        assertEquals(2, todoService.getCompletedCount());
        
        todoService.toggleTodo(todo1.getId()); // Toggle back to incomplete
        assertEquals(1, todoService.getCompletedCount());
    }

    @Test
    @DisplayName("Should count pending todos correctly")
    public void testGetPendingCount() {
        Todo todo1 = todoService.addTodo("First Todo");
        Todo todo2 = todoService.addTodo("Second Todo");
        Todo todo3 = todoService.addTodo("Third Todo");
        
        assertEquals(3, todoService.getPendingCount());
        
        todoService.completeTodo(todo1.getId());
        assertEquals(2, todoService.getPendingCount());
        
        todoService.completeTodo(todo2.getId());
        assertEquals(1, todoService.getPendingCount());
        
        todoService.completeTodo(todo3.getId());
        assertEquals(0, todoService.getPendingCount());
    }

    @Test
    @DisplayName("Should clear all todos")
    public void testClearAllTodos() {
        todoService.addTodo("First Todo");
        todoService.addTodo("Second Todo");
        todoService.addTodo("Third Todo");
        
        assertEquals(3, todoService.getTodoCount());
        
        todoService.clearAllTodos();
        
        assertEquals(0, todoService.getTodoCount());
        assertTrue(todoService.getAllTodos().isEmpty());
        
        // ID counter should be reset
        Todo newTodo = todoService.addTodo("New Todo");
        assertEquals(1, newTodo.getId());
    }

} 