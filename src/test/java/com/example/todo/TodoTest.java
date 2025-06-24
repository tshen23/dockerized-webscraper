package com.example.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Todo class
 */
@DisplayName("Todo Model Tests")
public class TodoTest {

    @Test
    @DisplayName("Should create todo with default constructor")
    public void testDefaultConstructor() {
        Todo todo = new Todo();
        
        assertEquals(0, todo.getId());
        assertNull(todo.getTitle());
        assertFalse(todo.isComplete());
    }

    @Test
    @DisplayName("Should create todo with id and title")
    public void testConstructorWithIdAndTitle() {
        Todo todo = new Todo(1, "Test Todo");
        
        assertEquals(1, todo.getId());
        assertEquals("Test Todo", todo.getTitle());
        assertFalse(todo.isComplete());
    }

    @Test
    @DisplayName("Should create todo with all parameters")
    public void testConstructorWithAllParameters() {
        Todo todo = new Todo(1, "Test Todo", true);
        
        assertEquals(1, todo.getId());
        assertEquals("Test Todo", todo.getTitle());
        assertTrue(todo.isComplete());
    }

    @Test
    @DisplayName("Should set and get id correctly")
    public void testSetAndGetId() {
        Todo todo = new Todo();
        todo.setId(42);
        
        assertEquals(42, todo.getId());
    }

    @Test
    @DisplayName("Should set and get title correctly")
    public void testSetAndGetTitle() {
        Todo todo = new Todo();
        todo.setTitle("New Title");
        
        assertEquals("New Title", todo.getTitle());
    }

    @Test
    @DisplayName("Should set and get complete status correctly")
    public void testSetAndGetComplete() {
        Todo todo = new Todo();
        todo.setComplete(true);
        
        assertTrue(todo.isComplete());
        
        todo.setComplete(false);
        assertFalse(todo.isComplete());
    }

    @Test
    @DisplayName("Should toggle complete status")
    public void testToggleComplete() {
        Todo todo = new Todo(1, "Test Todo", false);
        
        todo.toggleComplete();
        assertTrue(todo.isComplete());
        
        todo.toggleComplete();
        assertFalse(todo.isComplete());
    }

    @Test
    @DisplayName("Should implement equals correctly")
    public void testEquals() {
        Todo todo1 = new Todo(1, "Test Todo", false);
        Todo todo2 = new Todo(1, "Test Todo", false);
        Todo todo3 = new Todo(2, "Test Todo", false);
        Todo todo4 = new Todo(1, "Different Title", false);
        Todo todo5 = new Todo(1, "Test Todo", true);
        
        // Same object
        assertEquals(todo1, todo1);
        
        // Same content
        assertEquals(todo1, todo2);
        
        // Different id
        assertNotEquals(todo1, todo3);
        
        // Different title
        assertNotEquals(todo1, todo4);
        
        // Different complete status
        assertNotEquals(todo1, todo5);
        
        // Null comparison
        assertNotEquals(todo1, null);
        
        // Different class
        assertNotEquals(todo1, "string");
    }

    @Test
    @DisplayName("Should implement hashCode correctly")
    public void testHashCode() {
        Todo todo1 = new Todo(1, "Test Todo", false);
        Todo todo2 = new Todo(1, "Test Todo", false);
        
        assertEquals(todo1.hashCode(), todo2.hashCode());
    }

    @Test
    @DisplayName("Should implement toString correctly")
    public void testToString() {
        Todo todo = new Todo(1, "Test Todo", false);
        String expected = "Todo{id=1, title='Test Todo', complete=false}";
        
        assertEquals(expected, todo.toString());
    }
} 