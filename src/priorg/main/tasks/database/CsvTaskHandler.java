package priorg.main.tasks.database;


import priorg.main.tasks.TaskItem;

public class CsvTaskHandler extends CsvHandler {

    private static CsvTaskHandler instance;

    private CsvTaskHandler(DatabasePath dbPath) {
        super(dbPath);
    }

    @Override
    protected TaskItem parseItemImpl(String[] line) {
        return null;
    }

    public static CsvTaskHandler getInstance(DatabasePath dbPath) {
        if (instance == null) {
            instance = new CsvTaskHandler(dbPath);
        }
        return instance;
    }

}