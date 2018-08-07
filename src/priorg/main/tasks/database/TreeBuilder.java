package priorg.main.tasks.database;

import priorg.main.tasks.Category;
import priorg.main.tasks.TaskItem;

import java.util.Map;

public class TreeBuilder {

    private Map<Integer, TaskItem> categoryMap;

    public TreeBuilder() {
        categoryMap = CsvCategoryHandler.getInstance(DatabasePath.CATEGORIES).getItemsMap();
    }

}
