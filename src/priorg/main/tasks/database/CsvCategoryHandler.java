package priorg.main.tasks.database;


import priorg.main.tasks.Category;
import priorg.main.tasks.Id;

public class CsvCategoryHandler extends CsvHandler<Category> {

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
        Id id = new Id(Integer.parseInt(line[0]));
        Id parentId = new Id(Integer.parseInt(line[1]));
        String name = line[2];
        String description = line[3];
        String subCats = line[4];
        String subTasks = line[5];

        Category cat = new Category(id, name);
        cat.setDescription(description);
        cat.setParent(parentId);
        for (String subCat: subCats.split(",")) {
            cat.addCategoryById(new Id(Integer.parseInt(subCat)));
        }
        for (String subTask: subTasks.split(",")) {
            cat.addTaskById(new Id(Integer.parseInt(subTask)));
        }
        return cat;
    }

}
