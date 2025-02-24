package fr.arinonia.taskflow.views;

import com.jfoenix.controls.JFXButton;
import fr.arinonia.taskflow.controller.FilterController;
import fr.arinonia.taskflow.controller.TaskController;
import fr.arinonia.taskflow.util.ThemeColors;
import fr.arinonia.taskflow.util.ThemeSpacing;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainView {
    private final BorderPane root;
    private final HeaderView headerView;
    private final SidebarView sidebarView;
    private final TaskListView taskListView;
    private final TaskController taskController;
    private final FilterController filterController;

    private boolean isSidebarVisible = true;
    private boolean isCompactMode = false;

    public MainView() {
        this.root = new BorderPane();
        this.root.setId("main-container");

        this.taskController = new TaskController();
        this.filterController = new FilterController();

        this.headerView = new HeaderView();
        this.sidebarView = new SidebarView(this.filterController);
        this.taskListView = new TaskListView(this.taskController, this.filterController);

        setupUI();

        setupResponsiveLayout();
    }

    private void setupUI() {
        this.root.setStyle("-fx-background-color: #0f172a;");

        this.root.setTop(this.headerView.getRoot());

        this.root.setLeft(this.sidebarView.getRoot());

        final VBox centerContent = createCenterContent();

        final ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;" +
                        "-fx-border-width: 0;" +
                        "-fx-control-inner-background: transparent;" +
                        "-fx-padding: 0;"
        );

        this.root.setCenter(scrollPane);
    }

    private VBox createCenterContent() {
        final VBox centerContent = new VBox();
        centerContent.setSpacing(ThemeSpacing.SPACE_6);
        centerContent.setPadding(new Insets(
                ThemeSpacing.SPACE_6,
                ThemeSpacing.SPACE_6,
                ThemeSpacing.SPACE_6,
                ThemeSpacing.SPACE_6));
        centerContent.setStyle("-fx-background-color: #0f172a;");

        final DashboardStatsView statsView = new DashboardStatsView(this.taskController.getAllTasks());
        final HBox statsContainer = new HBox(statsView.getRoot());
        statsContainer.setAlignment(Pos.CENTER);

        statsView.getRoot().setOnMouseEntered(e -> {
            if (this.isCompactMode) {
                statsView.getRoot().setSpacing(5);
            }
        });
        statsView.getRoot().setOnMouseExited(e -> {
            if (this.isCompactMode) {
                statsView.getRoot().setSpacing(ThemeSpacing.SPACE_4);
            }
        });

        centerContent.getChildren().add(statsContainer);

        final HBox tasksHeader = new HBox();
        tasksHeader.setAlignment(Pos.CENTER_LEFT);
        tasksHeader.setSpacing(ThemeSpacing.SPACE_4);

        final Label tasksLabel = new Label("Mes tâches");
        tasksLabel.setFont(Font.font("System", FontWeight.BOLD, ThemeSpacing.TEXT_XL));
        tasksLabel.setTextFill(ThemeColors.GRAY_100);

        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        final JFXButton addTaskButton = new JFXButton("Ajouter une tâche");
        addTaskButton.getStyleClass().addAll("jfx-button", "button-primary");

        addTaskButton.setOnAction(event -> {
            DialogWrapper.showTaskDialog(this.root.getScene().getWindow(), this.taskController, task -> {
                if (task != null) {
                    this.taskListView.refresh();
                    statsView.refresh();
                }
            });
        });

        tasksHeader.getChildren().addAll(tasksLabel, spacer, addTaskButton);
        centerContent.getChildren().add(tasksHeader);

        VBox.setVgrow(this.taskListView.getRoot(), Priority.ALWAYS);
        centerContent.getChildren().add(this.taskListView.getRoot());

        this.taskListView.setOnTaskChanged(task -> {
            statsView.refresh();
        });

        return centerContent;
    }

    private void setupResponsiveLayout() {
        this.root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((prop, oldWin, newWin) -> {
                    if (newWin != null) {
                        newWin.widthProperty().addListener((o, oldWidth, newWidth) -> {
                            this.handleWindowResize(newWidth.doubleValue(), newWin.getHeight());
                        });

                        newWin.heightProperty().addListener((o, oldHeight, newHeight) -> {
                            this.handleWindowResize(newWin.getWidth(), newHeight.doubleValue());
                        });
                    }
                });
            }
        });
    }

    private void handleWindowResize(final double width, final double height) {
        if (width < 800) {
            if (!this.isCompactMode) {
                switchToCompactMode();
            }
        } else {
            if (this.isCompactMode) {
                switchToNormalMode();
            }
        }

        if (width < 650) {
            if (this.isSidebarVisible) {
                hideSidebar();
            }
        } else {
            if (!this.isSidebarVisible) {
                showSidebar();
            }
        }

        adaptComponentSizes(width, height);
    }

    private void switchToCompactMode() {
        this.isCompactMode = true;

        final VBox centerContent = (VBox) ((ScrollPane) this.root.getCenter()).getContent();
        centerContent.setSpacing(ThemeSpacing.SPACE_3);
        centerContent.setPadding(new Insets(
                ThemeSpacing.SPACE_3,
                ThemeSpacing.SPACE_3,
                ThemeSpacing.SPACE_3,
                ThemeSpacing.SPACE_3));

        final DashboardStatsView statsView = findStatsView(centerContent);
        if (statsView != null) {
            statsView.getRoot().setSpacing(ThemeSpacing.SPACE_2);
        }
    }

    private void switchToNormalMode() {
        this.isCompactMode = false;

        final VBox centerContent = (VBox) ((ScrollPane) root.getCenter()).getContent();
        centerContent.setSpacing(ThemeSpacing.SPACE_6);
        centerContent.setPadding(new Insets(
                ThemeSpacing.SPACE_6,
                ThemeSpacing.SPACE_6,
                ThemeSpacing.SPACE_6,
                ThemeSpacing.SPACE_6));

        final DashboardStatsView statsView = findStatsView(centerContent);
        if (statsView != null) {
            statsView.getRoot().setSpacing(ThemeSpacing.SPACE_4);
        }
    }

    private void hideSidebar() {
        this.isSidebarVisible = false;
        this.root.setLeft(null);

        this.headerView.showSidebarToggle(true, e -> {
            showSidebar();
            this.headerView.showSidebarToggle(false, null);
        });
    }


    private void showSidebar() {
        this.isSidebarVisible = true;
        this.root.setLeft(this.sidebarView.getRoot());

        this.headerView.showSidebarToggle(false, null);
    }

    private void adaptComponentSizes(double width, double height) {
        if (this.isSidebarVisible) {
            if (width < 900) {
                this.sidebarView.setWidth(180);
            } else {
                this.sidebarView.setWidth(240);
            }
        }

        this.taskListView.setMaxHeight(Math.max(350, height * 0.5));
    }


    private DashboardStatsView findStatsView(VBox centerContent) {
        if (!centerContent.getChildren().isEmpty()) {
            final Node firstChild = centerContent.getChildren().get(0);
            if (firstChild instanceof HBox) {
                final HBox statsContainer = (HBox) firstChild;
                if (!statsContainer.getChildren().isEmpty()) {
                    final Node statsNode = statsContainer.getChildren().get(0);
                    if (statsNode instanceof HBox) {
                        return new DashboardStatsView(this.taskController.getAllTasks()) {
                            @Override
                            public HBox getRoot() {
                                return (HBox) statsNode;
                            }
                        };
                    }
                }
            }
        }
        return null;
    }

    public BorderPane getRoot() {
        return this.root;
    }
}