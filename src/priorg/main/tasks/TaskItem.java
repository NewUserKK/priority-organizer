package priorg.main.tasks;

import com.opencsv.bean.CsvBindByName;
import priorg.main.tasks.database.CsvCategoryHandler;


/**
 * @author Konstantin Kostin
 */
public class TaskItem implements Comparable<TaskItem>, Identifiable {

    @CsvBindByName(column = "Parent ID")
    private Id parent;

    @CsvBindByName(column = "Name")
    private String name;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvBindByName(column = "ID")
    private Id id;

    private boolean root = false;


    public TaskItem(Id id, String name) {
        this.id = id;
        this.name = name;
    }

    public TaskItem(Id id, String name, boolean isRoot) {
        this.id = id;
        this.name = name;
        this.root = isRoot;
    }

    public void setName(String newName) {
//        try {
//            CsvHandler.getInstance().renameTaskItem(this, newName);
            this.name = newName;
//        } catch (DuplicateItemException e) {
//            System.err.println(e.getMessage());
//        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParent(Id parent) {
        this.parent = parent;
    }

    public Id getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getParent() {
        return CsvCategoryHandler.getInstance().getItemsMap().get(parent);
    }

    public boolean isRoot() {
        return root;
    }

    @Override
    public int compareTo(TaskItem other) {
        if (this.equals(other)) {
            return 0;
        }

        if (this instanceof Task && other instanceof Category) {
            return 1;
        }

        if (this instanceof Category && other instanceof Task) {
            return -1;
        }

        return this.getName().compareTo(other.getName());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
