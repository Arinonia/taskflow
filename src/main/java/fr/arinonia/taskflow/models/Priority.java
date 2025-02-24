package fr.arinonia.taskflow.models;

import javafx.scene.paint.Color;

public enum Priority {
    LOW("Faible", Color.web("#6699CC")),
    MEDIUM("Moyenne", Color.web("#F99157")),
    HIGH("Haute", Color.web("#EC5f67"));

    private final String name;
    private final Color color;

    Priority(final String name, final Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }
}
