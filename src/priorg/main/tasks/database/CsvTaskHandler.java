package priorg.main.tasks.database;


import priorg.main.Id;
import priorg.main.tasks.Category;
import priorg.main.tasks.Task;
import priorg.main.tasks.TaskItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CsvTaskHandler extends CsvHandler<TaskItem> {

    private static CsvTaskHandler instance;

    private CsvTaskHandler(DatabasePath dbPath) {
        super(dbPath);
    }

    public static CsvTaskHandler getInstance() {
        if (instance == null) {
            instance = new CsvTaskHandler(DatabasePath.TASKS);
        }
        return instance;
    }

    @Override
    protected Task parseItemImpl(String[] line) {
        // TODO: rewrite to opencsv annotations
        Id<TaskItem> id = new Id<>(Integer.parseInt(line[0]), TaskItem.class);
        Id<TaskItem> parentId = new Id<>(Integer.parseInt(line[1]), TaskItem.class);
        String name = line[2];
        String description = line[3];
        int priority = Integer.parseInt(line[4]);
        LocalDate deadline = extractDate(line[5]);

        Task task = new Task(id, name);
        task.setParentId(parentId);
        task.setDescription(description);
        task.setDeadline(deadline);
        task.setPriority(priority);

        return task;
    }

    private LocalDate extractDate(String rawDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date;
        try {
            date = LocalDate.parse(rawDate, formatter);
        } catch (DateTimeParseException e) {
            System.err.println(e.getMessage() + " setting date at 01.01.1970");
            date = LocalDate.of(1970, 1, 1);
        }
        return date;
    }

}