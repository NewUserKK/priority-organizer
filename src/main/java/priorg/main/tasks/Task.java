package priorg.main.tasks;

import com.opencsv.bean.CsvBindByName;
import priorg.main.id.Id;

import java.time.LocalDate;

/**
 * Class representing a task.
 * Task is a {@link TaskItem} with addition of priority and deadline.
 */
public class Task extends TaskItem {

    @CsvBindByName(column = "Priority")
    private int priority;

    @CsvBindByName(column = "Deadline")
    private LocalDate deadline;

    public Task(Id id, String name) {
        super(id, name);
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public int getPriority() {
        return priority;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
}
