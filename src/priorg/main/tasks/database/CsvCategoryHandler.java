package priorg.main.tasks.database;


public class CsvCategoryHandler extends CsvHandler {

    private static CsvCategoryHandler instance;

    private CsvCategoryHandler(DatabasePath dbPath) {
        super(dbPath);
    }

    public static CsvCategoryHandler getInstance(DatabasePath dbPath) {
        if (instance == null) {
            instance = new CsvCategoryHandler(dbPath);
        }
        return instance;
    }

}
