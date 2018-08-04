package priorg.main.tasks.database;


import java.io.File;

/**
 * @author Konstantin Kostin
 * */
public enum DatabasePaths {

    DIRECTORY("db"),
    OLD_DB_FILE("db_sample.txt"),
    OLD_DB_FULL_PATH(DIRECTORY.value + File.separator + OLD_DB_FILE.value),

    TASKS("tasks.csv"),
    CATEGORIES("categories.csv");

    private String value;

    DatabasePaths(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
