package priorg.main.tasks;

import com.opencsv.bean.CsvBindByName;
import priorg.main.id.Id;
import priorg.main.id.Identifiable;
import priorg.main.tasks.database.CsvCategoryHandler;
import priorg.main.tasks.database.CsvHandler;
import priorg.main.tasks.database.CsvTaskHandler;

import java.io.IOException;


/**
 * @author Konstantin Kostin
 */
public abstract class TaskItem implements Comparable<TaskItem>, Identifiable {

    @CsvBindByName(column = "Parent ID")
    private Id parentId;

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

    public void removeFromDb() {
        CsvHandler<TaskItem> db;
        if (this instanceof Category) {
            db = CsvCategoryHandler.getInstance();
        } else if (this instanceof Task) {
            db = CsvTaskHandler.getInstance();
        } else {
            throw new IllegalArgumentException("Cant find db for class " + getClass().getName());
        }

        try {
            db.removeEntry(this);
            // TODO: remove from subchildren
//            db.getItemById(parentId).
        } catch (IOException e) {
            System.err.println("Error while removing item " + getName() + " from db");
        }

    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParentId(Id parentId) {
        this.parentId = parentId;
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

    public Id getParentId() {
        return CsvCategoryHandler.getInstance().getItemById(parentId).getId();
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
