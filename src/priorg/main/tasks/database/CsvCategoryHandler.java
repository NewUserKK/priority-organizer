package priorg.main.tasks.database;


import priorg.main.id.CategoryId;
import priorg.main.id.TaskId;
import priorg.main.tasks.Category;
import priorg.main.id.Id;
import priorg.main.tasks.TaskItem;

/**
 * Singleton {@link CsvHandler} implementation for {@link Category}
 */
public class CsvCategoryHandler extends CsvHandler<TaskItem> {

    /** One and only instance of this class */
    private static CsvCategoryHandler instance;

    private CsvCategoryHandler(DatabasePath dbPath) {
        super(dbPath);
    }

    public static CsvCategoryHandler getInstance() {
        if (instance == null) {
            instance = new CsvCategoryHandler(DatabasePath.CATEGORIES);
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
     * 4 - subcategories represented with numbers (IDs), separated by space
     * 5 - subtasks represented with numbers (IDs), separated by space
     *
     * @param line line from CSV file to parse
     * @return Category object with info from parsed data
     */
    @Override
    protected Category parseItemImpl(String[] line) {
        // TODO: rewrite to opencsv annotations
        Id id = new CategoryId(Integer.parseInt(line[0]));
        Id parentId = new CategoryId(Integer.parseInt(line[1]));
        String name = line[2];
        String description = line[3];
        String subCats = line[4];
        String subTasks = line[5];

        Category cat = new Category(id, name);
        cat.setDescription(description);
        cat.setParentId(parentId);

        if (!subCats.isEmpty()) {
            for (String subCat: subCats.split(" ")) {
                cat.addCategoryById(new CategoryId(Integer.parseInt(subCat)));
            }
        }

        if (!subTasks.isEmpty()) {
            for (String subTask: subTasks.split(" ")) {
                cat.addTaskById(new TaskId(Integer.parseInt(subTask)));
            }
        }

        return cat;
    }

    @Override
    protected void removeEntryImpl(TaskItem item) {

    }

}
