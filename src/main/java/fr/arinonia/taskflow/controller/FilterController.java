package fr.arinonia.taskflow.controller;

import fr.arinonia.taskflow.models.Category;
import fr.arinonia.taskflow.models.Priority;
import fr.arinonia.taskflow.models.Task;
import fr.arinonia.taskflow.service.TaskService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class FilterController {
    public enum FilterType {
        ALL, TODO, COMPLETED, CATEGORY, PRIORITY
    }

    private ObjectProperty<FilterType> currentFilterType;
    private ObjectProperty<Category> selectedCategory;
    private ObjectProperty<Priority> selectedPriority;

    private ObservableList<Task> allTasks;
    private FilteredList<Task> filteredTasks;
    private TaskService taskService;

    public FilterController() {
        this.taskService = new TaskService();

        this.currentFilterType = new SimpleObjectProperty<>(FilterType.ALL);
        this.selectedCategory = new SimpleObjectProperty<>();
        this.selectedPriority = new SimpleObjectProperty<>();

        this.allTasks = taskService.getAllTasks();
        this.filteredTasks = new FilteredList<>(this.allTasks);

        applyFilter();

        this.currentFilterType.addListener((obs, oldVal, newVal) -> applyFilter());
        this.selectedCategory.addListener((obs, oldVal, newVal) -> {
            if (this.currentFilterType.get() == FilterType.CATEGORY) {
                applyFilter();
            }
        });
        this.selectedPriority.addListener((obs, oldVal, newVal) -> {
            if (this.currentFilterType.get() == FilterType.PRIORITY) {
                applyFilter();
            }
        });
    }

    private void applyFilter() {
        this.filteredTasks.setPredicate(task -> {
            switch (this.currentFilterType.get()) {
                case ALL:
                    return true;
                case TODO:
                    return !task.isCompleted();
                case COMPLETED:
                    return task.isCompleted();
                case CATEGORY:
                    return this.selectedCategory.get() == null || task.getCategory() == this.selectedCategory.get();
                case PRIORITY:
                    return this.selectedPriority.get() == null || task.getPriority() == this.selectedPriority.get();
                default:
                    return true;
            }
        });
    }

    public void showAllTasks() {
        this.currentFilterType.set(FilterType.ALL);
    }

    public void showTodoTasks() {
        this.currentFilterType.set(FilterType.TODO);
    }

    public void showCompletedTasks() {
        this.currentFilterType.set(FilterType.COMPLETED);
    }

    public void filterByCategory(final Category category) {
        this.selectedCategory.set(category);
        this.currentFilterType.set(FilterType.CATEGORY);
    }

    public void filterByPriority(final Priority priority) {
        this.selectedPriority.set(priority);
        this.currentFilterType.set(FilterType.PRIORITY);
    }

    public ObservableList<Task> getAllTasks() {
        return this.allTasks;
    }

    public FilteredList<Task> getFilteredTasks() {
        return this.filteredTasks;
    }

    public FilterType getCurrentFilterType() {
        return this.currentFilterType.get();
    }

    public ObjectProperty<FilterType> currentFilterTypeProperty() {
        return this.currentFilterType;
    }
}