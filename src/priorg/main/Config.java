package priorg.main;


/**
 * @author Konstantin Kostin
 * */
public enum Config {

    TASK_DB_PATH("src/priorg/main/tasks/db_sample.txt");

    private String value;

    Config(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
