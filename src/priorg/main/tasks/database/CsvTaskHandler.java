package priorg.main.tasks.database;


import priorg.main.id.Id;
import priorg.main.id.TaskId;
import priorg.main.tasks.Task;
import priorg.main.tasks.TaskItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Singleton {@link CsvHandler} implementation for the {@link Task}
 */
public class CsvTaskHandler extends CsvHandler<TaskItem> {

    /** One and only instance of this class */
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

    /**
     * Parse line according to the CSV file.
     * Current indexes:
     * 0 - id
     * 1 - id of a parent
     * 2 - name
     * 3 - description
     * 4 - priority of a task
     * 5 - deadline in format dd.MM.yyyy
     *
     * @param line line from CSV file to parse
     * @return Task object with info from parsed data
     */
    @Override
    protected Task parseItemImpl(String[] line) {
        // TODO: move to abstract
        // TODO: rewrite to opencsv annotations
        // TODO: add time to deadline
        Id id = new TaskId(Integer.parseInt(line[0]));
        Id parentId = new TaskId(Integer.parseInt(line[1]));
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

    @Override
    protected void removeEntryImpl(TaskItem item) {

    }

    /**
     * Transforming date string into the LocalDate object
     *
     * @param rawDate string containing the date
     * @return LocalDate object representing the same date
     */
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