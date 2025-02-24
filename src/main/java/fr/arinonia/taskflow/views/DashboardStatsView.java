package fr.arinonia.taskflow.views;

import fr.arinonia.taskflow.models.Category;
import fr.arinonia.taskflow.models.Priority;
import fr.arinonia.taskflow.models.Task;
import fr.arinonia.taskflow.util.ThemeColors;
import fr.arinonia.taskflow.util.ThemeSpacing;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


import java.util.HashMap;
import java.util.Map;

public class DashboardStatsView {
    private final HBox root;
    private final ObservableList<Task> tasks;

    public DashboardStatsView(final ObservableList<Task> tasks) {
        this.root = new HBox();
        this.tasks = tasks;
        setupUI();
    }

    private void setupUI() {
        this.root.setSpacing(ThemeSpacing.SPACE_4);
        this.root.setPadding(new Insets(0));
        this.root.setAlignment(Pos.CENTER);

        refreshStats();
    }

    public void refresh() {
        refreshStats();
    }

    private void refreshStats() {
        this.root.getChildren().clear();

        final int totalTasks = tasks.size();
        final long completedTasks = tasks.stream().filter(Task::isCompleted).count();
        final long pendingTasks = totalTasks - completedTasks;
        final double completionRate = totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0;

        this.root.getChildren().addAll(
                createStatCard("Total", String.valueOf(totalTasks), ThemeColors.PRIMARY_600),
                createStatCard("À faire", String.valueOf(pendingTasks), ThemeColors.INFO),
                createStatCard("Terminées", String.valueOf(completedTasks), ThemeColors.SUCCESS),
                createStatCard("Taux d'achèvement", String.format("%.0f%%", completionRate), ThemeColors.WARNING)
        );

        final Map<Category, Integer> categoryStats = new HashMap<>();
        for (final Category category : Category.values()) {
            categoryStats.put(category, 0);
        }

        for (Task task : this.tasks) {
            final Category category = task.getCategory();
            categoryStats.put(category, categoryStats.get(category) + 1);
        }

        Category topCategory = Category.PERSONAL;
        int maxTasks = 0;

        for (final Map.Entry<Category, Integer> entry : categoryStats.entrySet()) {
            if (entry.getValue() > maxTasks) {
                maxTasks = entry.getValue();
                topCategory = entry.getKey();
            }
        }

        if (maxTasks > 0) {
            this.root.getChildren().add(
                    createStatCard("Catégorie principale", topCategory.getName(), ThemeColors.PRIMARY_400)
            );
        }

        final Map<Priority, Integer> priorityStats = new HashMap<>();
        for (final Priority priority : Priority.values()) {
            priorityStats.put(priority, 0);
        }

        for (final Task task : tasks) {
            final Priority priority = task.getPriority();
            priorityStats.put(priority, priorityStats.get(priority) + 1);
        }

        Priority topPriority = Priority.MEDIUM;
        maxTasks = 0;

        for (final Map.Entry<Priority, Integer> entry : priorityStats.entrySet()) {
            if (entry.getValue() > maxTasks) {
                maxTasks = entry.getValue();
                topPriority = entry.getKey();
            }
        }

        if (maxTasks > 0) {
            this.root.getChildren().add(
                    createStatCard("Priorité principale", topPriority.getName(), topPriority.getColor())
            );
        }
    }

    private VBox createStatCard(final String title, final String value, final Color color) {
        final VBox card = new VBox();
        card.getStyleClass().add("stat-card");
        card.setAlignment(Pos.CENTER);
        card.setSpacing(ThemeSpacing.SPACE_1);
        card.setPadding(new Insets(
                ThemeSpacing.SPACE_4,
                ThemeSpacing.SPACE_5,
                ThemeSpacing.SPACE_4,
                ThemeSpacing.SPACE_5));
        card.setPrefWidth(180);

        final Circle colorIndicator = new Circle(5, color);

        final Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-title");

        final Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");

        final HBox titleBox = new HBox(ThemeSpacing.SPACE_2, colorIndicator, titleLabel);
        titleBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(titleBox, valueLabel);

        return card;
    }

    public HBox getRoot() {
        return this.root;
    }
}