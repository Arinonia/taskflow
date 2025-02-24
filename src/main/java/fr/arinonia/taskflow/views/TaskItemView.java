package fr.arinonia.taskflow.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import fr.arinonia.taskflow.controller.TaskController;
import fr.arinonia.taskflow.models.Priority;
import fr.arinonia.taskflow.models.Task;
import fr.arinonia.taskflow.util.ThemeColors;
import fr.arinonia.taskflow.util.ThemeSpacing;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.util.function.Consumer;

public class TaskItemView {
    private final HBox root;
    private final Task task;
    private final TaskController taskController;
    private Consumer<Task> onTaskChanged;
    private Consumer<Task> onTaskDeleted;

    private JFXCheckBox completedCheckBox;
    private Label titleLabel;
    private JFXButton deleteButton;
    private HBox badgesContainer;

    private Label descriptionLabel;
    private boolean isCompactMode = false;


    public TaskItemView(final Task task, final TaskController taskController) {
        this.root = new HBox();
        this.root.getStyleClass().add("task-item");
        this.task = task;
        this.taskController = taskController;
        setupUI();

        this.root.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            adaptToWidth(newWidth.doubleValue());
        });
    }

    private void setupUI() {
        this.root.setAlignment(Pos.CENTER_LEFT);
        this.root.setSpacing(ThemeSpacing.SPACE_3);
        this.root.setPadding(new Insets(12, 15, 12, 15));

        this.root.setOnMouseEntered(e ->
                this.root.setStyle("-fx-background-color: #334155;")
        );

        this.root.setOnMouseExited(e -> {
            if (this.task.isCompleted()) {
                this.root.setStyle("-fx-background-color: #1e293b; -fx-opacity: 0.8;");
            } else {
                this.root.setStyle("-fx-background-color: #1e293b;");
            }
        });

        this.completedCheckBox = new JFXCheckBox();
        this.completedCheckBox.setSelected(this.task.isCompleted());
        this.completedCheckBox.getStyleClass().add("jfx-check-box");

        this.completedCheckBox.setOnAction(e -> {
            this.task.setCompleted(this.completedCheckBox.isSelected());
            this.taskController.updateTask(this.task);
            updateTaskStyle();

            if (this.onTaskChanged != null) {
                this.onTaskChanged.accept(this.task);
            }
        });

        final Circle priorityIndicator = new Circle(6, this.task.getPriority().getColor());

        final VBox contentContainer = new VBox();
        contentContainer.setSpacing(ThemeSpacing.SPACE_1);
        contentContainer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        contentContainer.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(contentContainer, javafx.scene.layout.Priority.ALWAYS);

        this.titleLabel = new Label(this.task.getTitle());
        this.titleLabel.getStyleClass().add("task-title");
        this.titleLabel.setWrapText(true);
        this.titleLabel.setMaxWidth(Double.MAX_VALUE);

        if (this.task.getDescription() != null && !this.task.getDescription().isEmpty()) {
            this.descriptionLabel = new Label(this.task.getDescription());
            this.descriptionLabel.getStyleClass().add("task-description");
            this.descriptionLabel.setWrapText(true);
            this.descriptionLabel.setMaxWidth(Double.MAX_VALUE);

            final String description = this.task.getDescription();
            if (description.length() > 100) {
                this.descriptionLabel.setText(description.substring(0, 97) + "...");
                final Tooltip tooltip = new Tooltip(description);
                Tooltip.install(this.descriptionLabel, tooltip);
            } else {
                this.descriptionLabel.setText(description);
            }
            contentContainer.getChildren().addAll(this.titleLabel, this.descriptionLabel);
        } else {
            contentContainer.getChildren().add(this.titleLabel);
        }

        this.badgesContainer = new HBox();
        this.badgesContainer.setSpacing(ThemeSpacing.SPACE_2);
        this.badgesContainer.setAlignment(Pos.CENTER);
        this.badgesContainer.setMinWidth(100);

        final Label categoryBadge = createBadge(this.task.getCategory().getName(), "badge-primary");

        String priorityBadgeClass = "badge-low";
        if (this.task.getPriority() == Priority.MEDIUM) {
            priorityBadgeClass = "badge-medium";
        } else if (this.task.getPriority() == Priority.HIGH) {
            priorityBadgeClass = "badge-high";
        }

        final Label priorityBadge = createBadge(this.task.getPriority().getName(), priorityBadgeClass);

        this.badgesContainer.getChildren().addAll(categoryBadge, priorityBadge);

        this.deleteButton = new JFXButton("Supprimer");
        this.deleteButton.getStyleClass().addAll("jfx-button", "button-flat");
        this.deleteButton.setTextFill(ThemeColors.ERROR);
        this.deleteButton.setOnAction(e -> {
            final Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation");
            confirmDialog.setHeaderText("Supprimer la tâche");
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer cette tâche ?");

            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    this.taskController.deleteTask(this.task);

                    if (this.onTaskDeleted != null) {
                        this.onTaskDeleted.accept(this.task);
                    }
                }
            });
        });

        updateTaskStyle();

        this.root.getChildren().addAll(this.completedCheckBox, priorityIndicator, contentContainer, this.badgesContainer, this.deleteButton);
    }

    private void adaptToWidth(final double width) {
        if (width < 500 && !this.isCompactMode) {
            switchToCompactMode();
        } else if (width >= 500 && this.isCompactMode) {
            switchToNormalMode();
        }
    }

    private void switchToCompactMode() {
        this.isCompactMode = true;

        this.badgesContainer.setVisible(false);
        this.badgesContainer.setManaged(false);

        this.deleteButton.setText("");
        this.deleteButton.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 18px;");
        this.deleteButton.setText("×");

        this.root.setSpacing(ThemeSpacing.SPACE_2);
        this.root.setPadding(new Insets(8, 10, 8, 10));

        if (this.descriptionLabel != null) {
            this.descriptionLabel.setVisible(false);
            this.descriptionLabel.setManaged(false);
        }
    }

    private void switchToNormalMode() {
        this.isCompactMode = false;

        this.badgesContainer.setVisible(true);
        this.badgesContainer.setManaged(true);

        this.deleteButton.setText("Supprimer");
        this.deleteButton.setStyle("");

        this.root.setSpacing(ThemeSpacing.SPACE_3);
        this.root.setPadding(new Insets(12, 15, 12, 15));

        if (this.descriptionLabel != null) {
            this.descriptionLabel.setVisible(true);
            this.descriptionLabel.setManaged(true);
        }
    }

    private Label createBadge(final String text, final String styleClass) {
        final Label badge = new Label(text);
        badge.getStyleClass().addAll("badge", styleClass);
        return badge;
    }

    private void updateTaskStyle() {
        if (this.task.isCompleted()) {
            this.titleLabel.getStyleClass().add("completed-task");
            this.root.setStyle("-fx-background-color: #1e293b; -fx-opacity: 0.8;");
            this.root.getStyleClass().add("task-completed");
        } else {
            this.titleLabel.getStyleClass().remove("completed-task");
            this.root.setStyle("-fx-background-color: #1e293b;");
            this.root.getStyleClass().remove("task-completed");
        }
    }

    public void setOnTaskChanged(final Consumer<Task> onTaskChanged) {
        this.onTaskChanged = onTaskChanged;
    }

    public void setOnTaskDeleted(final Consumer<Task> onTaskDeleted) {
        this.onTaskDeleted = onTaskDeleted;
    }

    public HBox getRoot() {
        return this.root;
    }
}