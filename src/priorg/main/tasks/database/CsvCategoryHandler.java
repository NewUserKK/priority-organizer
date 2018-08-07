package priorg.main.tasks.database;


import priorg.main.tasks.TaskItem;

public class CsvCategoryHandler extends CsvHandler {

    private static CsvCategoryHandler instance;

    private CsvCategoryHandler(DatabasePath dbPath) {
        super(dbPath);
    }

    @Override
    protected TaskItem parseItemImpl(String[] line) {
        return null;
    }

    public static CsvCategoryHandler getInstance(DatabasePath dbPath) {
        if (instance == null) {
            instance = new CsvCategoryHandler(dbPath);
        }
        return instance;
    }

}
