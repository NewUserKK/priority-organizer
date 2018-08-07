package priorg.main.tasks.database;

import javafx.scene.control.TreeItem;
import priorg.main.tasks.Category;
import priorg.main.tasks.Id;
import priorg.main.tasks.Task;
import priorg.main.tasks.TaskItem;
import sun.reflect.generics.tree.Tree;

import java.util.Map;
import java.util.TreeMap;

public class TaskTreeBuilder {

    private final Map<Id, TreeItem<Category>> categoryMap;
    private final Map<Id, TreeItem<Task>> taskMap;

    public TaskTreeBuilder() {
        categoryMap = convertToTreeItem(CsvCategoryHandler.getInstance().getItemsMap());
        taskMap = convertToTreeItem(CsvTaskHandler.getInstance().getItemsMap());
    }

    private<T extends TaskItem> Map<Id, TreeItem<T>> convertToTreeItem(Map<Id, T> map) {
        Map<Id, TreeItem<T>> treeItemMap = new TreeMap<>();
        for (T item: map.values()) {
            treeItemMap.put(item.getId(), new TreeItem<>(item));
        }
        return treeItemMap;
    }

    public TreeItem<TaskItem> loadTree() {
        TreeItem<TaskItem> root = new TreeItem<>(new Category(new Id(-1), "TREE_ROOT"));

        for (TreeItem<Category> categoryItem: categoryMap.values()) {
            Category category = categoryItem.getValue();

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
