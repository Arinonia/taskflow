package fr.arinonia.taskflow.views;

import com.jfoenix.controls.JFXTextArea;
import fr.arinonia.taskflow.controller.TaskController;
import fr.arinonia.taskflow.models.Category;
import fr.arinonia.taskflow.models.Priority;
import fr.arinonia.taskflow.models.Task;
import fr.arinonia.taskflow.util.ThemeSpacing;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;

import java.util.function.Consumer;

public class DialogWrapper {

    public static void showTaskDialog(final Window parent, final TaskController taskController, final Consumer<Task> onTaskCreated) {
        final Stage dialogStage = new Stage();
        dialogStage.initOwner(parent);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initStyle(StageStyle.TRANSPARENT);

        final VBox dialogContent = new VBox();
        dialogContent.setSpacing(ThemeSpacing.SPACE_4);
        dialogContent.setPadding(new Insets(24));
        dialogContent.setMinWidth(500);
        dialogContent.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 8;");

        final DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        shadow.setRadius(10);
        dialogContent.setEffect(shadow);

        final Label dialogTitle = new Label("Ajouter une nouvelle tâche");
        dialogTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        dialogTitle.setTextFill(Color.WHITE);
        dialogTitle.setPadding(new Insets(0, 0, 16, 0));

        final Label titleLabel = new Label("Titre");
        titleLabel.setTextFill(Color.WHITE);
        final TextField titleField = new TextField();
        titleField.setPromptText("Titre de la tâche");
        styleTextField(titleField);

        final Label descriptionLabel = new Label("Description");
        descriptionLabel.setTextFill(Color.WHITE);
        final JFXTextArea descriptionField = new JFXTextArea();
        descriptionField.setPromptText("Description (optionnelle)");
        descriptionField.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #94a3b8;"
        );
        descriptionField.setPrefRowCount(3);
        descriptionField.setFocusColor(javafx.scene.paint.Color.web("#0ea5e9"));
        descriptionField.setUnFocusColor(javafx.scene.paint.Color.web("#475569"));
        styleTextArea(descriptionField);
        descriptionField.setPrefRowCount(3);

        final Label categoryLabel = new Label("Catégorie");
        categoryLabel.setTextFill(Color.WHITE);

        final ComboBox<Category> categoryComboBox = new ComboBox<>(FXCollections.observableArrayList(Category.values()));
        categoryComboBox.setPromptText("Sélectionnez une catégorie");
        categoryComboBox.setValue(Category.PERSONAL);

        ComboBoxStyles.styleCategoryComboBox(categoryComboBox);

        final Label priorityLabel = new Label("Priorité");
        priorityLabel.setTextFill(Color.WHITE);

        final ComboBox<Priority> priorityComboBox = new ComboBox<>(FXCollections.observableArrayList(Priority.values()));
        priorityComboBox.setPromptText("Sélectionnez une priorité");
        priorityComboBox.setValue(Priority.MEDIUM);

        ComboBoxStyles.stylePriorityComboBox(priorityComboBox);

        final HBox buttonBar = new HBox();
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.setSpacing(ThemeSpacing.SPACE_3);
        buttonBar.setPadding(new Insets(16, 0, 0, 0));

        final Button cancelButton = new Button("Annuler");
        styleButton(cancelButton, false);
        cancelButton.setOnAction(e -> dialogStage.close());

        final Button addButton = new Button("Ajouter");
        styleButton(addButton, true);
        addButton.setOnAction(e -> {
            final String title = titleField.getText().trim();

            if (title.isEmpty()) {
                titleField.setStyle("-fx-border-color: #ef4444; -fx-border-width: 2;");
                return;
            }

            final String description = descriptionField.getText().trim();
            final Category category = categoryComboBox.getValue();
            final Priority priority = priorityComboBox.getValue();

            final Task task = taskController.createTask(title, description, category, priority);

            dialogStage.close();

            if (onTaskCreated != null) {
                onTaskCreated.accept(task);
            }
        });

        buttonBar.getChildren().addAll(cancelButton, addButton);

        dialogContent.getChildren().addAll(
                dialogTitle,
                titleLabel, titleField,
                descriptionLabel, descriptionField,
                categoryLabel, categoryComboBox,
                priorityLabel, priorityComboBox,
                buttonBar);

        final StackPane dialogContainer = new StackPane(dialogContent);
        dialogContainer.setPadding(new Insets(20));
        dialogContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0);"); // Fond sombre semi-transparent

        final Scene dialogScene = new Scene(dialogContainer);
        dialogScene.setFill(Color.TRANSPARENT);

        dialogContainer.setOnMouseClicked(event -> {
            if (event.getTarget() == dialogContainer) {
                dialogStage.close();
            }
            event.consume();
        });

        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    private static void styleTextField(final TextField textField) {
        textField.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #94a3b8;" +
                        "-fx-background-radius: 4;" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-radius: 4;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 8;"
        );
    }

    private static void styleTextArea(final TextArea textArea) {
        textArea.setStyle(
                "-fx-control-inner-background: #334155;" +
                        "-fx-background-color: #334155;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #94a3b8;" +
                        "-fx-highlight-fill: #0ea5e9;" +
                        "-fx-highlight-text-fill: white;" +
                        "-fx-background-radius: 4;" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-radius: 4;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 4;"
        );

        textArea.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textArea.setStyle(
                        "-fx-control-inner-background: #334155;" +
                                "-fx-background-color: #334155;" +
                                "-fx-text-fill: white;" +
                                "-fx-prompt-text-fill: #94a3b8;" +
                                "-fx-highlight-fill: #0ea5e9;" +
                                "-fx-highlight-text-fill: white;" +
                                "-fx-background-radius: 4;" +
                                "-fx-border-color: #0ea5e9;" +
                                "-fx-border-radius: 4;" +
                                "-fx-border-width: 0;" +
                                "-fx-padding: 4;"
                );
            } else {
                textArea.setStyle(
                        "-fx-control-inner-background: #334155;" +
                                "-fx-background-color: #334155;" +
                                "-fx-text-fill: white;" +
                                "-fx-prompt-text-fill: #94a3b8;" +
                                "-fx-highlight-fill: #0ea5e9;" +
                                "-fx-highlight-text-fill: white;" +
                                "-fx-background-radius: 4;" +
                                "-fx-border-color: #475569;" +
                                "-fx-border-radius: 4;" +
                                "-fx-border-width: 1;" +
                                "-fx-padding: 4;"
                );
            }
        });
    }

    private static void styleComboBox(final ComboBox<?> comboBox) {
        comboBox.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #94a3b8;" +
                        "-fx-background-radius: 4;" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-radius: 4;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 4;"
        );
    }

    private static void styleButton(final Button button, final boolean isPrimary) {
        if (isPrimary) {
            button.setStyle(
                    "-fx-background-color: #0ea5e9;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 4;" +
                            "-fx-padding: 8 16;" +
                            "-fx-cursor: hand;"
            );
        } else {
            button.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: white;" +
                            "-fx-border-color: #475569;" +
                            "-fx-border-radius: 4;" +
                            "-fx-background-radius: 4;" +
                            "-fx-padding: 8 16;" +
                            "-fx-cursor: hand;"
            );
        }

        button.setOnMouseEntered(e -> {
            if (isPrimary) {
                button.setStyle(
                        "-fx-background-color: #38bdf8;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 4;" +
                                "-fx-padding: 8 16;" +
                                "-fx-cursor: hand;"
                );
            } else {
                button.setStyle(
                        "-fx-background-color: rgba(71, 85, 105, 0.2);" +
                                "-fx-text-fill: white;" +
                                "-fx-border-color: #475569;" +
                                "-fx-border-radius: 4;" +
                                "-fx-background-radius: 4;" +
                                "-fx-padding: 8 16;" +
                                "-fx-cursor: hand;"
                );
            }
        });

        button.setOnMouseExited(e -> {
            if (isPrimary) {
                button.setStyle(
                        "-fx-background-color: #0ea5e9;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 4;" +
                                "-fx-padding: 8 16;" +
                                "-fx-cursor: hand;"
                );
            } else {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: white;" +
                                "-fx-border-color: #475569;" +
                                "-fx-border-radius: 4;" +
                                "-fx-background-radius: 4;" +
                                "-fx-padding: 8 16;" +
                                "-fx-cursor: hand;"
                );
            }
        });
    }
}