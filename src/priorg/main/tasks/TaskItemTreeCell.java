package priorg.main.tasks;

import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;


/**
 * @author Konstantin Kostin
 * */
public class TaskItemTreeCell extends TreeCell<TaskItem> {

    private TextField editField;

    public TaskItemTreeCell() {
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (editField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(editField);
        editField.setText(getItemName());
        editField.selectAll();
    }

    private void createTextField() {
        editField = new TextField();
        editField.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                TaskItem item = getItem();
                item.setName(editField.getText());
                commitEdit(item);
            } else if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItemName());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    public void commitEdit(TaskItem newValue) {
        super.commitEdit(newValue);
        setText(newValue.getName());
    }

    @Override
    protected void updateItem(TaskItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);

        } else {
            if (isEditing()) {
                if (editField != null) {
                    editField.setText(getItemName());
                }
                setText(null);
                setGraphic(editField);

            } else {
                setText(getItemName());
                setGraphic(getTreeItem().getGraphic());
            }
        }
    }

    private String getItemName() {
        return getItem() != null ? getItem().toString() : "";
    }
}