package priorg.main.tasks;

import priorg.main.Config;

import java.io.*;


/**
 * @author Konstantin Kostin
 */
public class DatabaseUtils {

    private String dbPath;

    public DatabaseUtils(String dbPath) {
        this.dbPath = dbPath;
    }

    public void renameTaskItem(String oldText, String newText) {
        try (BufferedReader oldDb = new BufferedReader(
                new InputStreamReader(new FileInputStream(dbPath)));
             PrintWriter newDb = new PrintWriter(dbPath + ".tmp")) {

            String line;
            while ((line = oldDb.readLine()) != null) {
                if (isComment(line) || line.isEmpty()) {
                    newDb.write(line);

                } else {
                    String[] categorySplit = line.split(">");
                    renameCategory(oldText, newText, newDb, categorySplit);

                    String[] taskSplit = categorySplit[categorySplit.length - 1].split("\\|");
                    renameTask(oldText, newText, newDb, taskSplit);
                }

                newDb.write("\n");
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        renameFile();
    }

    private void renameFile() {
        File oldDbFile = new File(Config.TASK_DB_PATH.toString());
        File newDbFile = new File(Config.TASK_DB_PATH.toString() + ".tmp");
        if (oldDbFile.delete()) {
            newDbFile.renameTo(new File(Config.TASK_DB_PATH.toString()));
        }
    }

    private void renameTask(String oldText, String newText, PrintWriter newDb, String[] taskSplit) {
        if (taskSplit[0].equals(oldText)) {
            newDb.write(newText + "|");
        } else {
            newDb.write(taskSplit[0] + "|");
        }
        for (int i = 1; i < taskSplit.length; i++) {
            newDb.write(taskSplit[i] + (i == taskSplit.length - 1 ? "" : "|"));
        }
    }

    private void renameCategory(String oldText, String newText, PrintWriter newDb, String[] categorySplit) {
        for (int i = 0; i < categorySplit.length - 1; i++) {
            String cat = categorySplit[i];
            if (cat.equals(oldText)) {
                newDb.write(newText);
            } else {
                newDb.write(cat);
            }
            newDb.write(">");
        }
    }

    public static boolean isComment(String line) {
        return line.startsWith("#");
    }
}
