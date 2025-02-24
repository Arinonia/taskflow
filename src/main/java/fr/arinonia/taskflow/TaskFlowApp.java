package fr.arinonia.taskflow;

import fr.arinonia.taskflow.util.ThemeColors;
import fr.arinonia.taskflow.views.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TaskFlowApp extends Application {
    @Override
    public void start(final Stage stage) {
        final MainView mainView = new MainView();

        final Scene scene = new Scene(mainView.getRoot(), 1280, 740);

        scene.setFill(ThemeColors.BG_DARK);

        try {
            scene.getStylesheets().add(getClass().getResource("/css/jfoenix-styles.css").toExternalForm());
        } catch (final Exception e) {
            System.err.println("Impossible de charger le fichier CSS: " + e.getMessage());
            e.printStackTrace();
        }

        stage.setTitle("TaskFlow");
        stage.setScene(scene);
        stage.setMinWidth(1280);
        stage.setMinHeight(740);

        stage.show();
    }
}