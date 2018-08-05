package priorg.main.tasks.database;

import priorg.main.tasks.Category;

import java.util.Map;

public class TreeBuilder {

    private Map<Integer, Category> categoryMap;

    public TreeBuilder() {
        categoryMap = new CsvCategoryHandler().buildMap();
    }

}
