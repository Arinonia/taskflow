package fr.arinonia.taskflow.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.arinonia.taskflow.models.Task;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static final String DATA_DIRECTORY = "data";
    private static final String TASKS_FILE = "tasks.json";
    private final Gson gson;

    public DataStore() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();

        createDataDirectory();
    }

    private void createDataDirectory() {
        final File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public List<Task> loadTasks() {
        final File file = new File(DATA_DIRECTORY, TASKS_FILE);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (final FileReader reader = new FileReader(file)) {
            final Type taskListType = new TypeToken<ArrayList<Task>>(){}.getType();
            final List<Task> tasks = this.gson.fromJson(reader, taskListType);

            return tasks != null ? tasks : new ArrayList<>();
        } catch (final IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveTasks(final List<Task> tasks) {
        final File file = new File(DATA_DIRECTORY, TASKS_FILE);

        try (final FileWriter writer = new FileWriter(file)) {
            this.gson.toJson(tasks, writer);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
