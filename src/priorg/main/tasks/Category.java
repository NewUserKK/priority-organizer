package priorg.main.tasks;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Konstantin Kostin
 */
public class Category extends TaskItem {

    private Set<TaskItem> subItems = new TreeSet<>();

    public Category(String name) {
        super(name);
    }


    public void addItem(TaskItem item) {
        subItems.add(item);
    }

    public Set<TaskItem> getSubItems() {
        return subItems;
    }

}
