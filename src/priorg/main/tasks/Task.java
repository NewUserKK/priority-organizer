package priorg.main.tasks;

import java.util.Date;

/**
 * @author Konstantin Kostin
 */
public class Task extends TaskItem {

    private int priority;
    private Date deadline;

    public Task(String name) {
        super(name);
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

}
