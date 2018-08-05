package priorg.main.tasks.database;

import priorg.main.tasks.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Konstantin Kostin
 * */
public abstract class CsvHandler implements AutoCloseable {

    private static int itemCounter;

    private CsvDatabaseFile dbFile;
    private Map<Integer, TaskItem> items;

    protected CsvHandler(DatabasePath dbPath) {
        this.dbFile = new CsvDatabaseFile(dbPath);
        this.items = new HashMap<>();
        openDbFile(CsvDatabaseFile.READ);
        buildMap();
    }

    private void openDbFile(String mode) {
        try {
            dbFile.open(mode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * =====================
     * | Tree load from db |
     * =====================
     * */

    private void buildMap() {
        openDbFile(CsvDatabaseFile.READ);

        try {
            dbFile.skip(1);

            String[] line;
            while ((line = dbFile.readNext()) != null) {
                TaskItem parsedItem = parseItem(line);
                items.put(parsedItem.getId(), parsedItem);
            }

        } catch (IOException e) {
            System.err.println("Error while loading tree from db");
            e.printStackTrace();
        }
    }

    public TaskItem parseItem(String[] line) {
        itemCounter++;
        return parseItemImpl(line);
    }

    protected abstract TaskItem parseItemImpl(String[] line);

    public Map<Integer, TaskItem> getItemsMap() {
        return items;
    }

    /**
     * =================
     * | Task renaming |
     * =================
     * */

    public void renameTaskItem(TaskItem item, String newName) throws DuplicateNameException {
        checkDuplicates(item);
        openDbFile(CsvDatabaseFile.READ);

        String oldName = item.getName();
    }

    /**
     * =================
     * | Task addition |
     * =================
     * */

    public void addEntry(TaskItem newItem) throws DuplicateNameException {
        checkDuplicates(newItem);
        openDbFile(CsvDatabaseFile.APPEND);
    }


    /**
     * =========
     * | Utils |
     * =========
     * */

    public void checkDuplicates(TaskItem item) throws DuplicateNameException {
        if (items.containsKey(item.getId())) {
            throw new DuplicateNameException("Task or category already exists!");
        }
    }

    @Override
    public void close() throws IOException {
        dbFile.close();
    }
}
