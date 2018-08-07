package priorg.main.tasks.database;


import priorg.main.tasks.Category;
import priorg.main.Id;
import priorg.main.tasks.Task;
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
        Id<TaskItem> id = new Id<>(Integer.parseInt(line[0]), TaskItem.class);
        Id<TaskItem> parentId = new Id<>(Integer.parseInt(line[1]), TaskItem.class);
        String name = line[2];
        String description = line[3];
        String subCats = line[4];
        String subTasks = line[5];

        Category cat = new Category(id, name);
        cat.setDescription(description);
        cat.setParentId(parentId);
        if (!subCats.isEmpty()) {
            for (String subCat: subCats.split(" ")) {
                cat.addCategoryById(new Id<>(Integer.parseInt(subCat), TaskItem.class));
            }
        }

        if (!subTasks.isEmpty()) {
            for (String subTask: subTasks.split(" ")) {
                cat.addTaskById(new Id<>(Integer.parseInt(subTask), TaskItem.class));
            }
        }

        return cat;
    }

}
