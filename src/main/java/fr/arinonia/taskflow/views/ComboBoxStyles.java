package fr.arinonia.taskflow.views;

import fr.arinonia.taskflow.models.Category;
import fr.arinonia.taskflow.models.Priority;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ComboBoxStyles {

    public static void stylePriorityComboBox(final ComboBox<Priority> priorityComboBox) {
        priorityComboBox.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #94a3b8;" +
                        "-fx-background-radius: 4;" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-radius: 4;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 4;"
        );

        priorityComboBox.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                priorityComboBox.setOnShown(e -> stylePopup(priorityComboBox));
            }
        });

        priorityComboBox.setCellFactory(createPriorityCellFactory());
        priorityComboBox.setButtonCell(createPriorityCell());
    }

    public static void styleCategoryComboBox(final ComboBox<Category> categoryComboBox) {
        categoryComboBox.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #94a3b8;" +
                        "-fx-background-radius: 4;" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-radius: 4;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 4;"
        );

        categoryComboBox.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                categoryComboBox.setOnShown(e -> stylePopup(categoryComboBox));
            }
        });

        categoryComboBox.setCellFactory(createCategoryCellFactory());
        categoryComboBox.setButtonCell(createCategoryCell());
    }

    private static void stylePopup(final ComboBox<?> comboBox) {
        try {
            Platform.runLater(() -> {
                final Node popup = comboBox.lookup(".combo-box-popup");
                if (popup != null) {
                    popup.setStyle("-fx-background-color: #1e293b;");

                    final ListView<?> listView = (ListView<?>) popup.lookup(".list-view");
                    if (listView != null) {
                        listView.setStyle(
                                "-fx-background-color: #1e293b;" +
                                        "-fx-background: #1e293b;" +
                                        "-fx-control-inner-background: #1e293b;" +
                                        "-fx-border-color: #475569;" +
                                        "-fx-border-width: 0px;" +
                                        "-fx-padding: 2px;"
                        );

                        final Region scrollPane = (Region) listView.lookup(".virtual-flow");
                        if (scrollPane != null) {
                            scrollPane.setStyle("-fx-border-width: 0; -fx-background-color: transparent;");
                        }
                    }

                    final ScrollBar scrollBar = (ScrollBar) popup.lookup(".scroll-bar");
                    if (scrollBar != null) {
                        scrollBar.setStyle("-fx-background-color: transparent;");

                        final Node thumb = scrollBar.lookup(".thumb");
                        if (thumb != null) {
                            thumb.setStyle("-fx-background-color: #475569; -fx-background-radius: 3;");
                        }

                        final Node track = scrollBar.lookup(".track");
                        if (track != null) {
                            track.setStyle("-fx-background-color: transparent;");
                        }
                    }
                }
            });
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static Callback<ListView<Category>, ListCell<Category>> createCategoryCellFactory() {
        return new Callback<ListView<Category>, ListCell<Category>>() {
            @Override
            public ListCell<Category> call(final ListView<Category> param) {
                return new ListCell<Category>() {
                    @Override
                    protected void updateItem(final Category item, final boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            this.setText(null);
                        } else {
                            this.setText(item.getName());
                            this.setTextFill(Color.WHITE);
                            this.setStyle("-fx-background-color: #334155;");
                        }

                        this.selectedProperty().addListener((o, oldVal, newVal) -> {
                            if (newVal) {
                                this.setStyle("-fx-background-color: #0ea5e9;");
                            } else {
                                this.setStyle("-fx-background-color: #334155;");
                            }
                        });
                    }
                };
            }
        };
    }

    private static ListCell<Category> createCategoryCell() {
        return new ListCell<Category>() {
            @Override
            protected void updateItem(final Category item, final boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    this.setText(null);
                } else {
                    this.setText(item.getName());
                    this.setTextFill(Color.WHITE);
                    this.setStyle("-fx-background-color: #334155;");
                }
            }
        };
    }

    private static Callback<ListView<Priority>, ListCell<Priority>> createPriorityCellFactory() {
        return new Callback<ListView<Priority>, ListCell<Priority>>() {
            @Override
            public ListCell<Priority> call(final ListView<Priority> param) {
                return new ListCell<Priority>() {
                    @Override
                    protected void updateItem(final Priority item, final boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            this.setText(null);
                        } else {
                            this.setText(item.getName());
                            this.setTextFill(item.getColor());
                            this.setStyle("-fx-background-color: #334155;");
                        }

                        this.selectedProperty().addListener((o, oldVal, newVal) -> {
                            if (newVal) {
                                this.setStyle("-fx-background-color: #0ea5e9;");
                            } else {
                                this.setStyle("-fx-background-color: #334155;");
                            }
                        });
                    }
                };
            }
        };
    }

    private static ListCell<Priority> createPriorityCell() {
        return new ListCell<Priority>() {
            @Override
            protected void updateItem(final Priority item, final boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    this.setText(null);
                } else {
                    this.setText(item.getName());
                    this.setTextFill(item.getColor());
                    this.setStyle("-fx-background-color: #334155;");
                }
            }
        };
    }
}