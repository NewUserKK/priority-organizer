package priorg.main.tasks;

import priorg.main.id.Id;
import priorg.main.tasks.database.CsvCategoryHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class representing category of tasks.
 * May have subcategories and subtasks into itself.
 */
public class Category extends TaskItem {

    private Set<Id> subCategories = new HashSet<>();
    private Set<Id> subTasks = new HashSet<>();


    public Category(Id id, String name) {
        super(id, name);
//        CsvCategoryHandler.getInstance().getItemsMap().put(id, this);
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
}
