package fr.arinonia.taskflow.views;

import com.jfoenix.controls.JFXButton;
import fr.arinonia.taskflow.util.ThemeSpacing;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HeaderView {
    private final HBox root;
    private JFXButton menuButton;

    public HeaderView() {
        this.root = new HBox();
        this.root.setId("header");
        this.root.getStyleClass().add("header");
        setupUI();
    }

    private void setupUI() {
        this.root.setAlignment(Pos.CENTER_LEFT);
        this.root.setPadding(new Insets(
                ThemeSpacing.SPACE_4,
                ThemeSpacing.SPACE_6,
                ThemeSpacing.SPACE_4,
                ThemeSpacing.SPACE_6));

        final DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        this.root.setEffect(shadow);

        final StackPane menuContainer = new StackPane();
        menuContainer.setPrefWidth(32);
        menuContainer.setMinWidth(32);
        menuContainer.setAlignment(Pos.CENTER_LEFT);
        this.root.getChildren().add(menuContainer);

        final HBox titleContainer = new HBox();
        titleContainer.setAlignment(Pos.CENTER_LEFT);
        titleContainer.setSpacing(10);

        final Label titleLabel = new Label("TaskFlow");
        titleLabel.getStyleClass().add("title-label");

        final Label subtitleLabel = new Label(" • Gérez vos tâches plus simplement");
        subtitleLabel.getStyleClass().add("subtitle-label");

        titleContainer.getChildren().addAll(titleLabel, subtitleLabel);

        final Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        final HBox actionsContainer = new HBox();
        actionsContainer.setAlignment(Pos.CENTER_RIGHT);
        actionsContainer.setSpacing(8);

        final JFXButton refreshButton = new JFXButton("Actualiser");
        refreshButton.getStyleClass().addAll("jfx-button", "button-flat");

        final JFXButton settingsButton = new JFXButton("Paramètres");
        settingsButton.getStyleClass().addAll("jfx-button", "button-flat");

        actionsContainer.getChildren().addAll(refreshButton, settingsButton);

        this.root.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            if (newWidth.doubleValue() < 600) {
                if (titleContainer.getChildren().contains(subtitleLabel)) {
                    titleContainer.getChildren().remove(subtitleLabel);
                }

                if (!refreshButton.getText().isEmpty()) {
                    refreshButton.setText("");
                    refreshButton.setGraphic(createIcon("refresh"));
                    settingsButton.setText("");
                    settingsButton.setGraphic(createIcon("settings"));
                }
            } else {
                if (!titleContainer.getChildren().contains(subtitleLabel)) {
                    titleContainer.getChildren().add(subtitleLabel);
                }

                if (refreshButton.getText().isEmpty()) {
                    refreshButton.setText("Actualiser");
                    refreshButton.setGraphic(null);
                    settingsButton.setText("Paramètres");
                    settingsButton.setGraphic(null);
                }
            }
        });

        this.root.getChildren().addAll(titleContainer, spacer, actionsContainer);
    }

    public void showSidebarToggle(final boolean show, final EventHandler<ActionEvent> onAction) {
        final StackPane menuContainer = (StackPane) this.root.getChildren().get(0);

        if (show) {
            if (this.menuButton == null) {
                this.menuButton = new JFXButton();
                this.menuButton.getStyleClass().addAll("jfx-button", "button-flat");
                this.menuButton.setGraphic(createIcon("menu"));
                this.menuButton.setStyle("-fx-padding: 4;");
            }

            this.menuButton.setOnAction(onAction);
            menuContainer.getChildren().setAll(this.menuButton);
        } else {
            menuContainer.getChildren().clear();
        }
    }

    private Region createIcon(final String iconName) {
        final Region icon = new Region();
        icon.setPrefSize(18, 18);
        icon.setMinSize(18, 18);
        icon.setMaxSize(18, 18);

        String color = "#ffffff";
        if (iconName.equals("menu")) {
            color = "#f8fafc";
        } else if (iconName.equals("refresh")) {
            color = "#38bdf8";
        } else if (iconName.equals("settings")) {
            color = "#94a3b8";
        }

        icon.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-shape: 'M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z';" // Menu icon shape
        );

        return icon;
    }

    public HBox getRoot() {
        return this.root;
    }
}