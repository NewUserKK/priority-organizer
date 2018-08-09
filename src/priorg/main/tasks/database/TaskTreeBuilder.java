package priorg.main.tasks.database;

import javafx.scene.control.TreeItem;
import priorg.main.id.CategoryId;
import priorg.main.tasks.Category;
import priorg.main.id.Id;
import priorg.main.tasks.TaskItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder for {@link javafx.scene.control.TreeView} of {@link TaskItem} from the database.
 */
public class TaskTreeBuilder {

    private final Map<Id, TreeItem<TaskItem>> categoryMap;
    private final Map<Id, TreeItem<TaskItem>> taskMap;

    /**
     * Default constructor.
     * Wraps all data from database into {@link TreeItem} and copies it into corresponding maps.
     */
    public TaskTreeBuilder() {
        categoryMap = convertToTreeItem(CsvCategoryHandler.getInstance().getItemsMap());
        taskMap = convertToTreeItem(CsvTaskHandler.getInstance().getItemsMap());
    }

    /**
     * Wraps data from given map into a map of {@link TreeItem}
     *
     * @param map map with data
     * @param <T> type of objects in a map
     * @return map with the same objects wrapped in {@link TreeItem}
     */
    private<T extends TaskItem> Map<Id, TreeItem<T>> convertToTreeItem(Map<Id, T> map) {
        Map<Id, TreeItem<T>> treeItemMap = new HashMap<>();
        for (T item: map.values()) {
            treeItemMap.put(item.getId(), new TreeItem<>(item));
        }
        return treeItemMap;
    }

    /**
     * Creates {@link javafx.scene.control.TreeView} from the map and return its root
     * Basically iterates over categories and its children making connections between all of them.
     *
     * @return root of built tree
     */
    public TreeItem<TaskItem> loadTree() {
        // TODO: alphabetic sorting
        TreeItem<TaskItem> root = new TreeItem<>(
                new Category(new CategoryId(-1), "TREE_ROOT"));
        categoryMap.put(root.getValue().getId(), root);

        for (TreeItem<TaskItem> categoryItem: categoryMap.values()) {
            if (categoryItem.equals(root)) {
                continue;
            }

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
