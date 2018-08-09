package priorg.main.tasks.database;

import javafx.scene.control.TreeItem;
import priorg.main.id.CategoryId;
import priorg.main.tasks.Category;
import priorg.main.id.Id;
import priorg.main.tasks.TaskItem;

import java.util.HashMap;
import java.util.Map;

public class TaskTreeBuilder {

    private final Map<Id, TreeItem<TaskItem>> categoryMap;
    private final Map<Id, TreeItem<TaskItem>> taskMap;

    public TaskTreeBuilder() {
        categoryMap = convertToTreeItem(CsvCategoryHandler.getInstance().getItemsMap());
        taskMap = convertToTreeItem(CsvTaskHandler.getInstance().getItemsMap());
    }

    private<T extends TaskItem> Map<Id, TreeItem<T>> convertToTreeItem(Map<Id, T> map) {
        Map<Id, TreeItem<T>> treeItemMap = new HashMap<>();
        for (T item: map.values()) {
            treeItemMap.put(item.getId(), new TreeItem<>(item));
        }
        return treeItemMap;
    }

    public TreeItem<TaskItem> loadTree() {
        // TODO: alphabetic sorting
        TreeItem<TaskItem> root = new TreeItem<>(
                new Category(new CategoryId(-1), "TREE_ROOT"));

        for (TreeItem<TaskItem> categoryItem: categoryMap.values()) {
            Category category = (Category) categoryItem.getValue();

            if (category.getParentId().equals(root.getValue().getId())) {
                root.getChildren().add(categoryItem);
            }

            for (Id subCatId: category.getSubCategories()) {
                categoryItem.getChildren().add(categoryMap.get(subCatId));
            }

            for (Id subTaskId: category.getSubTasks()) {
                categoryItem.getChildren().add(taskMap.get(subTaskId));
            }
        }

        return root;
    }

}
