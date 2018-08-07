package priorg.main.tasks;

import priorg.main.tasks.database.CsvHandler;

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

    public Category(String name, boolean isRoot) {
        super(name, isRoot);
    }


    public void addItem(TaskItem item) throws DuplicateNameException {
        addItem(item, true);
    }

    public void addItem(TaskItem item, boolean writeToDb) throws DuplicateNameException {
        // TODO: move to abstract
//        CsvHandler.getInstance().checkDuplicates(item);
        subItems.add(item);
        item.setParent(this);
        if (writeToDb) {
//            CsvHandler.getInstance().addEntry(item);
        }
    }

    public Set<TaskItem> getSubItems() {
        return subItems;
    }

}
