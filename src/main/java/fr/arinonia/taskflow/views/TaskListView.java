package fr.arinonia.taskflow.views;

import fr.arinonia.taskflow.controller.FilterController;
import fr.arinonia.taskflow.controller.TaskController;
import fr.arinonia.taskflow.models.Task;
import fr.arinonia.taskflow.util.ThemeColors;
import fr.arinonia.taskflow.util.ThemeSpacing;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class TaskListView {
    private final VBox root;
    private final TaskController taskController;
    private final FilterController filterController;
    private Consumer<Task> onTaskChanged;

    private ScrollPane scrollPane;
    private VBox tasksContainer;
    private double maxHeight = 350;

    public TaskListView(final TaskController taskController, final FilterController filterController) {
        this.root = new VBox();
        this.taskController = taskController;
        this.filterController = filterController;
        setupUI();
    }

    private void setupUI() {
        this.root.setSpacing(ThemeSpacing.SPACE_2);
        this.root.setPadding(new Insets(
                ThemeSpacing.SPACE_2,
                ThemeSpacing.SPACE_2,
                ThemeSpacing.SPACE_2,
                ThemeSpacing.SPACE_2));
        this.root.getStyleClass().add("form-container");

        this.scrollPane = new ScrollPane();
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        this.scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;" +
                        "-fx-border-width: 0;" +
                        "-fx-control-inner-background: transparent;" +
                        "-fx-padding: 0;"
        );

        this.scrollPane.setMaxHeight(this.maxHeight);

        this.tasksContainer = new VBox();
        this.tasksContainer.setSpacing(10);
        this.tasksContainer.setPadding(new Insets(5));
        this.tasksContainer.getStyleClass().add("task-container");

        this.scrollPane.setContent(this.tasksContainer);

        this.root.getChildren().add(this.scrollPane);

        final FilteredList<Task> filteredTasks = this.filterController.getFilteredTasks();
        filteredTasks.addListener((ListChangeListener<Task>) change -> {
            refreshTaskList(filteredTasks);
        });

        this.filterController.currentFilterTypeProperty().addListener((obs, oldVal, newVal) -> {
            refreshTaskList(filteredTasks);
        });

        refreshTaskList(filteredTasks);
    }

    private void refreshTaskList(final FilteredList<Task> tasks) {
        this.tasksContainer.getChildren().clear();

        if (tasks.isEmpty()) {
            final Label emptyLabel = new Label("Aucune tâche à afficher");
            emptyLabel.setTextFill(ThemeColors.GRAY_500);
            emptyLabel.setPadding(new Insets(ThemeSpacing.SPACE_6));
            emptyLabel.setAlignment(Pos.CENTER);
            emptyLabel.setMaxWidth(Double.MAX_VALUE);
            emptyLabel.setMinHeight(200);
            this.tasksContainer.getChildren().add(emptyLabel);
            return;
        }

        for (final Task task : tasks) {
            final TaskItemView taskItemView = new TaskItemView(task, this.taskController);

            taskItemView.setOnTaskChanged(changedTask -> {
                if (this.onTaskChanged != null) {
                    this.onTaskChanged.accept(changedTask);
                }
            });

            taskItemView.setOnTaskDeleted(deletedTask -> {
                refresh();
                if (this.onTaskChanged != null) {
                    this.onTaskChanged.accept(null);
                }
            });

            this.tasksContainer.getChildren().add(taskItemView.getRoot());

            if (tasks.indexOf(task) < tasks.size() - 1) {
                final Region spacer = new Region();
                spacer.setPrefHeight(1);
                spacer.setMinHeight(1);
                spacer.setMaxHeight(1);
                spacer.setStyle("-fx-background-color: #334155;");
                this.tasksContainer.getChildren().add(spacer);
            }
        }
    }

    public void setMaxHeight(double height) {
        this.maxHeight = height;
        if (this.scrollPane != null) {
            this.scrollPane.setMaxHeight(height);
        }
    }


    public void refresh() {
        refreshTaskList(this.filterController.getFilteredTasks());
    }

    public void setOnTaskChanged(final Consumer<Task> onTaskChanged) {
        this.onTaskChanged = onTaskChanged;
    }

    public VBox getRoot() {
        return this.root;
    }
}