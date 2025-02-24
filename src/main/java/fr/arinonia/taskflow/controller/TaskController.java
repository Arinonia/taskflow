package fr.arinonia.taskflow.controller;

import fr.arinonia.taskflow.models.Category;
import fr.arinonia.taskflow.models.Priority;
import fr.arinonia.taskflow.models.Task;
import fr.arinonia.taskflow.service.TaskService;
import javafx.collections.ObservableList;

public class TaskController {
    private final TaskService taskService;

    public TaskController() {
        this.taskService = new TaskService();
    }

    public Task createTask(final String title) {
        final Task task = new Task(title);
        this.taskService.addTask(task);
        return task;
    }

    public Task createTask(final String title, final String description, final Category category, final Priority priority) {
        final Task task = new Task(title, description, category, priority);
        this.taskService.addTask(task);
        return task;
    }

    public void updateTask(final Task task, final String title, final String description, final Category category, final Priority priority) {
        task.setTitle(title);
        task.setDescription(description);
        task.setCategory(category);
        task.setPriority(priority);
        this.taskService.updateTask(task);
    }

    public void updateTask(final Task task) {
        this.taskService.updateTask(task);
    }

    public void deleteTask(final Task task) {
        this.taskService.deleteTask(task);
    }

    public void setTaskCompleted(final Task task, final boolean completed) {
        task.setCompleted(completed);
        this.taskService.updateTask(task);
    }

    public ObservableList<Task> getAllTasks() {
        this.taskService.getAllTasks().forEach(System.out::println);
        return this.taskService.getAllTasks();
    }
}