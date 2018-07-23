package priorg.main.tasks;

import priorg.main.Config;


/**
 * @author Konstantin Kostin
 */
public class TaskItem implements Comparable<TaskItem> {

    private Category parent;
    private String name;
    private String description;

    public TaskItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        // TODO: check duplicates
        new DatabaseUtils(Config.TASK_DB_PATH.toString()).renameTaskItem(name, newName);
        this.name = newName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParent(Category parent) {
        this.parent = parent;
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
