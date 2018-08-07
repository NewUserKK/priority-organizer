package priorg.main.tasks;

import priorg.main.tasks.database.CsvHandler;


/**
 * @author Konstantin Kostin
 */
public class TaskItem implements Comparable<TaskItem> {

    private Category parent;
    private String name;
    private String description;
    private int id; // TODO: id

    private boolean root = false;

    public TaskItem(String name) {
        this.name = name;
    }

    public TaskItem(String name, boolean isRoot) {
        this.name = name;
        this.root = isRoot;
    }

    public void setName(String newName) {
//        try {
//            CsvHandler.getInstance().renameTaskItem(this, newName);
            this.name = newName;
//        } catch (DuplicateNameException e) {
//            System.err.println(e.getMessage());
//        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getParent() {
        return parent;
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
