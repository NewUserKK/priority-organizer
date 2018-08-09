package priorg.main.tasks.database;


import priorg.main.id.CategoryId;
import priorg.main.id.TaskId;
import priorg.main.tasks.Category;
import priorg.main.id.Id;
import priorg.main.tasks.TaskItem;

public class CsvCategoryHandler extends CsvHandler<TaskItem> {

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

}
