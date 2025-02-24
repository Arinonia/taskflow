package fr.arinonia.taskflow.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Task {
    private final String id;
    private String title;
    private String description;
    private boolean completed;
    private Category category;
    private Priority priority;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task() {
        this.id = UUID.randomUUID().toString();
        this.completed = false;
        this.category = Category.PERSONAL;
        this.priority = Priority.MEDIUM;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Task(final String title) {
        this();
        this.title = title;
    }

    public Task(final String title, final String description, final Category category, final Priority priority) {
        this();
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void setCompleted(final boolean completed) {
        this.completed = completed;
        this.updatedAt = LocalDateTime.now();
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(final Category category) {
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + this.id + '\'' +
                ", title='" + this.title + '\'' +
                ", completed=" + this.completed +
                ", category=" + this.category +
                ", priority=" + this.priority +
                '}';
    }
}

