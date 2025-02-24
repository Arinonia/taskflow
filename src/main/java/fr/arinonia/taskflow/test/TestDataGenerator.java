package fr.arinonia.taskflow.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.arinonia.taskflow.models.Category;
import fr.arinonia.taskflow.models.Priority;
import fr.arinonia.taskflow.models.Task;
import fr.arinonia.taskflow.util.LocalDateTimeAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TestDataGenerator {

    private static final String DATA_DIRECTORY = "data";
    private static final String TASKS_FILE = "tasks.json";
    private static final Random random = new Random();

    public static void main(String[] args) {
        final List<Task> tasks = generateTestTasks();
        saveTasksToFile(tasks);
        System.out.println("Données de test générées avec succès dans " + DATA_DIRECTORY + "/" + TASKS_FILE);
    }

    private static List<Task> generateTestTasks() {
        final List<Task> tasks = new ArrayList<>();

        for (final Category category : Category.values()) {
            final Task task = new Task("Tâche " + category.getName(),
                    "Description de la tâche pour la catégorie " + category.getName(),
                    category, Priority.MEDIUM);
            tasks.add(task);
        }

        for (final Priority priority : Priority.values()) {
            final Task task = new Task("Tâche priorité " + priority.getName(),
                    "Description de la tâche avec priorité " + priority.getName(),
                    Category.PERSONAL, priority);
            tasks.add(task);
        }

        final Task completedTask = new Task("Tâche terminée",
                "Cette tâche est marquée comme terminée",
                Category.WORK, Priority.HIGH);
        completedTask.setCompleted(true);
        tasks.add(completedTask);

        final Task todoTask = new Task("Tâche à faire",
                "Cette tâche n'est pas encore terminée",
                Category.WORK, Priority.HIGH);
        tasks.add(todoTask);

        final Task oldTask = new Task("Ancienne tâche",
                "Cette tâche a été créée il y a une semaine",
                Category.STUDY, Priority.LOW);
        tasks.add(oldTask);

        final Task longTitleTask = new Task(
                "Tâche avec un titre très long pour tester l'affichage des titres longs dans l'interface utilisateur",
                "Description courte",
                Category.HEALTH, Priority.MEDIUM);
        tasks.add(longTitleTask);

        final Task longDescTask = new Task("Tâche avec description longue",
                "Cette tâche a une description très longue pour tester l'affichage des descriptions longues. " +
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. " +
                        "Suspendisse lectus tortor, dignissim sit amet, adipiscing nec, ultricies sed, dolor. " +
                        "Cras elementum ultrices diam. Maecenas ligula massa, varius a, semper congue, euismod non, mi. " +
                        "Proin porttitor, orci nec nonummy molestie, enim est eleifend mi, non fermentum diam nisl sit amet erat.",
                Category.FINANCE, Priority.HIGH);
        tasks.add(longDescTask);

        final Task noDescTask = new Task("Tâche sans description");
        noDescTask.setCategory(Category.SHOPPING);
        noDescTask.setPriority(Priority.LOW);
        tasks.add(noDescTask);

        for (int i = 0; i < 15; i++) {
            tasks.add(generateRandomTask(i));
        }

        return tasks;
    }

    private static Task generateRandomTask(int index) {
        final Category[] categories = Category.values();
        final Priority[] priorities = Priority.values();

        final Category randomCategory = categories[random.nextInt(categories.length)];
        final Priority randomPriority = priorities[random.nextInt(priorities.length)];
        final boolean isCompleted = random.nextBoolean();

        final Task task = new Task(
                "Tâche aléatoire " + index,
                "Description aléatoire pour la tâche " + index,
                randomCategory,
                randomPriority
        );

        task.setCompleted(isCompleted);
        return task;
    }

    private static void saveTasksToFile(List<Task> tasks) {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();

        final File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        final File file = new File(directory, TASKS_FILE);
        try (final FileWriter writer = new FileWriter(file)) {
            gson.toJson(tasks, writer);
        } catch (final IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}