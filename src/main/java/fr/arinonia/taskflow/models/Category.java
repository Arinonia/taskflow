package fr.arinonia.taskflow.models;

public enum Category {
    PERSONAL("Personnel"),
    WORK("Professionnel"),
    STUDY("Études"),
    HEALTH("Santé"),
    FINANCE("Finances"),
    SHOPPING("Achats"),
    OTHER("Autre");

    private final String name;

    Category(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
