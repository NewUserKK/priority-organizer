package priorg.main.tasks;

import java.time.LocalDate;

/**
 * @author Konstantin Kostin
 */
public class Task extends TaskItem {

    private int priority;
    private LocalDate deadline;

    public Task(String name) {
        super(name);
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

}
