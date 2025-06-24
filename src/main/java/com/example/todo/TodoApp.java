package com.example.todo;

import java.util.Scanner;

/**
 * TodoApp - A simple command-line todo application
 * This is the main entry point for the application
 */
public class TodoApp {
    private final TodoService todoService;
    private final Scanner scanner;

    public TodoApp() {
        this.todoService = new TodoService();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        TodoApp app = new TodoApp();
        app.run();
    }

    public void run() {
        System.out.println("Welcome to the Todo Application!");
        System.out.println("Commands: list, add <title>, complete <id>, delete <id>, quit");
        
        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();
            
            if (input.equals("quit")) {
                System.out.println("Goodbye!");
                break;
            }
            
            processCommand(input);
        }
        
        scanner.close();
    }

    private void processCommand(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        
        switch (command) {
            case "list":
                listTodos();
                break;
            case "add":
                if (parts.length > 1) {
                    addTodo(parts[1]);
                } else {
                    System.out.println("Please provide a title for the todo");
                }
                break;
            case "complete":
                if (parts.length > 1) {
                    try {
                        int id = Integer.parseInt(parts[1]);
                        completeTodo(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid todo ID");
                    }
                } else {
                    System.out.println("Please provide a todo ID to complete");
                }
                break;
            case "delete":
                if (parts.length > 1) {
                    try {
                        int id = Integer.parseInt(parts[1]);
                        deleteTodo(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid todo ID");
                    }
                } else {
                    System.out.println("Please provide a todo ID to delete");
                }
                break;
            default:
                System.out.println("Unknown command. Available commands: list, add <title>, complete <id>, delete <id>, quit");
        }
    }

    private void listTodos() {
        var todos = todoService.getAllTodos();
        if (todos.isEmpty()) {
            System.out.println("No todos found");
        } else {
            System.out.println("\nTodos:");
            for (Todo todo : todos) {
                String status = todo.isComplete() ? "[✓]" : "[ ]";
                System.out.printf("%s %d. %s%n", status, todo.getId(), todo.getTitle());
            }
        }
    }

    private void addTodo(String title) {
        Todo todo = todoService.addTodo(title);
        System.out.printf("Added todo: %d. %s%n", todo.getId(), todo.getTitle());
    }

    private void completeTodo(int id) {
        if (todoService.completeTodo(id)) {
            System.out.printf("Completed todo with ID: %d%n", id);
        } else {
            System.out.printf("Todo with ID %d not found%n", id);
        }
    }

    private void deleteTodo(int id) {
        if (todoService.deleteTodo(id)) {
            System.out.printf("Deleted todo with ID: %d%n", id);
        } else {
            System.out.printf("Todo with ID %d not found%n", id);
        }
    }
} 