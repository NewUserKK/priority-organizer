package priorg.main.tasks.database;

import priorg.main.tasks.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Konstantin Kostin
 * */
public class TasksDatabase implements AutoCloseable {

    private static TasksDatabase instance;

    private final String DB_PATH = DatabasePaths.OLD_DB_FULL_PATH.toString();
    private final DatabaseFile dbFile = new DatabaseFile(DB_PATH);

    private Map<String, TaskItem> tasksMap;
    private Category root;

    private TasksDatabase() {
        this.tasksMap = new HashMap<>();
        openDbFile(DatabaseFile.READ);
    }

    private void openDbFile(String mode) {
        try {
            dbFile.open(mode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TasksDatabase getInstance() {
        if (instance == null) {
            instance = new TasksDatabase();
        }
        return instance;
    }


    /**
     * =====================
     * | Tree load from db |
     * =====================
     * */

    public void loadTree(Category root) {
        this.root = root;
        tasksMap.put("TREE_ROOT", root);

        openDbFile(DatabaseFile.READ);

        try {
            String line;
            while ((line = dbFile.readLine()) != null) {
                if (isComment(line) || line.equals("")) {
                    continue;
                }

                String[] categoriesSplit = line.split(">");
                parseCategories(categoriesSplit);

                String[] taskSplit = categoriesSplit[categoriesSplit.length - 1].split("\\|");
                parseTask(taskSplit, (Category) tasksMap.get(categoriesSplit[categoriesSplit.length - 2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DuplicateNameException e) {
            System.err.println("Found duplicate while loading database");
        }
    }

    private void parseCategories(String[] categoriesSplit) throws DuplicateNameException {
        for (int i = 0; i < categoriesSplit.length - 1; i++) {
            String categoryName = categoriesSplit[i];
            if (!tasksMap.containsKey(categoryName)) {
                Category newCategory = new Category(categoryName);
                Category parent = (i == 0 ? root : (Category) tasksMap.get(categoriesSplit[i - 1]));
                newCategory.setParent(parent);
                parent.addItem(newCategory, false);
                tasksMap.put(categoryName, newCategory);
            }
        }
    }

    private void parseTask(String[] taskSplit, Category parentCategory) throws DuplicateNameException {
        Task task = new Task(taskSplit[0]);
        parentCategory.addItem(task, false);
        task.setParent(parentCategory);
        task.setPriority(Integer.parseInt(taskSplit[1]));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            task.setDeadline(LocalDate.parse(taskSplit[2], formatter));
        } catch (DateTimeParseException e) {
            System.err.println(e.getMessage() + " setting date at 01.01.1970");
            task.setDeadline(LocalDate.of(1970, 1, 1));
        }
        task.setDescription(taskSplit[3]);

        tasksMap.put(task.getName(), task);
    }


    /**
     * =================
     * | Task renaming |
     * =================
     * */

    public void renameTaskItem(TaskItem item, String newName) throws DuplicateNameException {
        checkDuplicates(item);
        openDbFile(DatabaseFile.READ);

        String oldName = item.getName();

        try (PrintWriter newDb = new PrintWriter(DB_PATH + ".tmp")) {
            String line;
            while ((line = dbFile.readLine()) != null) {
                if (isComment(line) || line.isEmpty()) {
                    newDb.write(line);

                } else {
                    String[] categorySplit = line.split(">");
                    renameCategory(oldName, newName, newDb, categorySplit);

                    String[] taskSplit = categorySplit[categorySplit.length - 1].split("\\|");
                    renameTask(oldName, newName, newDb, taskSplit);
                }

                newDb.write("\n");
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        renameFile();
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

    private void renameFile() {
        File oldDbFile = new File(DatabasePaths.OLD_DB_FULL_PATH.toString());
        File newDbFile = new File(DatabasePaths.OLD_DB_FULL_PATH.toString() + ".tmp");
        if (oldDbFile.delete()) {
            newDbFile.renameTo(new File(DatabasePaths.OLD_DB_FULL_PATH.toString()));
        }
    }


    /**
     * =================
     * | Task addition |
     * =================
     * */

    public void addEntry(TaskItem newItem) throws DuplicateNameException {
        checkDuplicates(newItem);
        openDbFile(DatabaseFile.APPEND);

        ArrayList<String> pathToItem = new ArrayList<>(8);
        TaskItem parent = newItem.getParent();
        while (!parent.isRoot()) {
            pathToItem.add(parent.getName());
            parent = parent.getParent();
        }

        StringBuilder resultPath = new StringBuilder();
        for (int i = pathToItem.size() - 1; i >= 0; i--) {
            resultPath.append(pathToItem.get(i)).append(">");
        }

        String taskInfo = String.format("%s|1|23.04.1234|%s", newItem.getName(),
                newItem.getDescription());

        dbFile.println(resultPath + taskInfo);

        tasksMap.put(newItem.getName(), newItem);
    }


    /**
     * =========
     * | Utils |
     * =========
     * */

    public void checkDuplicates(TaskItem item) throws DuplicateNameException {
        if (tasksMap.containsKey(item.getName())) {
            throw new DuplicateNameException("Task or category already exists!");
        }
    }

    private boolean isComment(String line) {
        return line.startsWith("#");
    }

    @Override
    public void close() throws IOException {
        dbFile.close();
    }
}
