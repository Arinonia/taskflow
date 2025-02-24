package fr.arinonia.taskflow.views;

import com.jfoenix.controls.JFXButton;
import fr.arinonia.taskflow.controller.FilterController;
import fr.arinonia.taskflow.models.Category;
import fr.arinonia.taskflow.models.Priority;
import fr.arinonia.taskflow.util.ThemeColors;
import fr.arinonia.taskflow.util.ThemeSpacing;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SidebarView {
    private final VBox root;
    private final FilterController filterController;

    public SidebarView(final FilterController filterController) {
        this.root = new VBox();
        this.root.setId("sidebar");
        this.root.getStyleClass().add("sidebar");
        this.filterController = filterController;
        setupUI();
    }

    private void setupUI() {
        this.root.setPrefWidth(240);
        this.root.setMinWidth(240);
        this.root.setMaxWidth(240);
        this.root.setSpacing(ThemeSpacing.SPACE_2);

        addStatusFilters();

        addSeparator();

        addCategoryFilters();

        addSeparator();

        addPriorityFilters();
    }

    private void addStatusFilters() {
        final Label sectionLabel = new Label("FILTRES");
        sectionLabel.getStyleClass().add("section-label");
        this.root.getChildren().add(sectionLabel);

        final JFXButton allTasksButton = createSidebarButton("Toutes les tâches", ThemeColors.PRIMARY_500);
        allTasksButton.setOnAction(event -> this.filterController.showAllTasks());
        this.root.getChildren().add(allTasksButton);

        final JFXButton todoButton = createSidebarButton("À faire", ThemeColors.INFO);
        todoButton.setOnAction(event -> this.filterController.showTodoTasks());
        this.root.getChildren().add(todoButton);

        final JFXButton doneButton = createSidebarButton("Terminées", ThemeColors.SUCCESS);
        doneButton.setOnAction(event -> this.filterController.showCompletedTasks());
        this.root.getChildren().add(doneButton);
    }

    private void addCategoryFilters() {
        final Label sectionLabel = new Label("CATÉGORIES");
        sectionLabel.getStyleClass().add("section-label");
        this.root.getChildren().add(sectionLabel);

        for (final Category category : Category.values()) {
            final JFXButton categoryButton = createSidebarButton(category.getName(), ThemeColors.PRIMARY_400);
            categoryButton.setOnAction(event -> this.filterController.filterByCategory(category));
            this.root.getChildren().add(categoryButton);
        }
    }

    private void addPriorityFilters() {
        final Label sectionLabel = new Label("PRIORITÉS");
        sectionLabel.getStyleClass().add("section-label");
        this.root.getChildren().add(sectionLabel);

        for (final Priority priority : Priority.values()) {
            final JFXButton priorityButton = createSidebarButton(priority.getName(), priority.getColor());
            priorityButton.setOnAction(event -> this.filterController.filterByPriority(priority));
            this.root.getChildren().add(priorityButton);
        }
    }

    private JFXButton createSidebarButton(final String text, final Color iconColor) {
        final JFXButton button = new JFXButton(text);
        button.getStyleClass().add("sidebar-item");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);

        final Circle indicator = new Circle(6, iconColor);
        button.setGraphic(indicator);
        button.setGraphicTextGap(10);

        return button;
    }

    private void addSeparator() {
        final Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #334155;");
        this.root.getChildren().add(separator);

        final Region spacer = new Region();
        spacer.setPrefHeight(ThemeSpacing.SPACE_2);
        this.root.getChildren().add(spacer);
    }

    public void setWidth(final double width) {
        this.root.setPrefWidth(width);
        this.root.setMinWidth(width);
        this.root.setMaxWidth(width);

        if (width <= 180) {
            this.root.setPadding(new Insets(
                    ThemeSpacing.SPACE_4,
                    ThemeSpacing.SPACE_2,
                    ThemeSpacing.SPACE_4,
                    ThemeSpacing.SPACE_2));
        } else {
            this.root.setPadding(new Insets(
                    ThemeSpacing.SPACE_6,
                    ThemeSpacing.SPACE_4,
                    ThemeSpacing.SPACE_6,
                    ThemeSpacing.SPACE_4));
        }
    }

    public VBox getRoot() {
        return this.root;
    }
}