package fr.arinonia.taskflow.service;


import fr.arinonia.taskflow.models.Task;
import fr.arinonia.taskflow.util.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskService {
    private final ObservableList<Task> tasks;
    private final DataStore dataStore;


    public TaskService() {
        this.dataStore = new DataStore();
        this.tasks = FXCollections.observableArrayList();

        loadTasks();
    }

    private void loadTasks() {
        this.tasks.clear();
        this.tasks.addAll(this.dataStore.loadTasks());
    }


    private void saveTasks() {
        this.dataStore.saveTasks(this.tasks);
    }


    public void addTask(final Task task) {
        this.tasks.add(task);
        saveTasks();
    }


    public void updateTask(final Task task) {
        saveTasks();
    }

    public void deleteTask(final Task task) {
        this.tasks.remove(task);
        saveTasks();
    }

    public ObservableList<Task> getAllTasks() {
        return this.tasks;
    }
}