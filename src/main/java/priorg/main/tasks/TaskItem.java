package priorg.main.tasks;

import com.opencsv.bean.CsvBindByName;
import priorg.main.id.Id;
import priorg.main.id.Identifiable;
import priorg.main.tasks.database.CsvCategoryHandler;
import priorg.main.tasks.database.CsvHandler;
import priorg.main.tasks.database.CsvTaskHandler;

import java.io.IOException;


/**
 * Class representing any item that refers to the tasks. Currently it is {@link Task} and {@link Category}.
 * It has unique ID, name, description and parent.
 *
 * Task item can be compared with another item in the following way:
 *   1. If items have not the same class then Category is "greater", then a Task
 *   2. Otherwise they are compared as their names - alphabetically
 */
public abstract class TaskItem implements Comparable<TaskItem>, Identifiable {

    @CsvBindByName(column = "ID")
    private Id id;

    @CsvBindByName(column = "Parent ID")
    private Id parentId;

    @CsvBindByName(column = "Name")
    private String name;

    @CsvBindByName(column = "Description")
    private String description;


    public TaskItem(Id id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Remove item from database.
     * @see CsvHandler
     */
    public void removeFromDb() {
        CsvHandler<TaskItem> db = getDbInstance();

        try {
            db.removeEntry(this);
        } catch (IOException e) {
            System.err.println("Error while removing item " + getName() + " from db");
        }

    }

    private CsvHandler<TaskItem> getDbInstance() {
        if (this instanceof Category) {
            return CsvCategoryHandler.getInstance();
        } else if (this instanceof Task) {
            return CsvTaskHandler.getInstance();
        } else {
            throw new IllegalArgumentException("Cant find db for class " + getClass().getName());
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
        return parentId;
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
    public boolean equals(Object obj) {
        // TODO: proper fields equals
        if (this == obj) {
            return true;
        }
        if (getClass().equals(obj.getClass())) {
            return id == ((TaskItem) obj).id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
