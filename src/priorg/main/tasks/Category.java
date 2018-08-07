package priorg.main.tasks;

import priorg.main.tasks.database.CsvCategoryHandler;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Konstantin Kostin
 */
public class Category extends TaskItem {

    private Set<Id> subCategories = new TreeSet<>();
    private Set<Id> subTasks = new TreeSet<>();


    public Category(Id id, String name) {
        this(id, name, false);
    }

    public Category(Id id, String name, boolean isRoot) {
        super(id, name, isRoot);
        CsvCategoryHandler.getInstance().getItemsMap().put(id, this);
    }

    public Set<Id> getSubCategories() {
        return subCategories;
    }

    public Set<Id> getSubTasks() {
        return subTasks;
    }

    public void addCategoryById(Id id0, Id ... ids) {
        addItemById(subCategories, id0, ids);
    }

    public void addTaskById(Id id0, Id ... ids) {
        addItemById(subTasks, id0, ids);
    }

    private void addItemById(Set<Id> set, Id id0, Id ... ids) {
        set.add(id0);
        set.addAll(Arrays.asList(ids));
    }


    public void addItem(TaskItem item) {
        addItem(item, true);
    }

    public void addItem(TaskItem item, boolean writeToDb) {
        // TODO: move to abstract?
//        CsvCategoryHandler.getInstance().checkDuplicates(item);
        if (item instanceof Category) {
            subCategories.add(item.getId());
        } else {
            subTasks.add(item.getId());
        }
//        item.setParent(this);
        if (writeToDb) {
//            CsvHandler.getInstance().addEntry(item);
        }
    }

//    public Set<TaskItem> getSubItems() {
//        return subItems;
//    }

}
