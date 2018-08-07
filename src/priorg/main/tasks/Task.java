package priorg.main.tasks;

import com.opencsv.bean.CsvBindByName;
import priorg.main.Id;

import java.time.LocalDate;

/**
 * @author Konstantin Kostin
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
