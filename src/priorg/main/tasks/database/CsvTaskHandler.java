package priorg.main.tasks.database;


public class CsvTaskHandler extends CsvHandler {

    private static CsvTaskHandler instance;

    private CsvTaskHandler(DatabasePath dbPath) {
        super(dbPath);
    }

    public static CsvTaskHandler getInstance(DatabasePath dbPath) {
        if (instance == null) {
            instance = new CsvTaskHandler(dbPath);
        }
        return instance;
    }

}