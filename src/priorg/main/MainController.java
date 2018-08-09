package priorg.main;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import priorg.about.AboutWindow;
import priorg.buttons.AddEditWindow;
import priorg.main.tasks.*;
import priorg.main.tasks.database.CsvHandler;
import priorg.main.tasks.database.CsvTaskHandler;
import priorg.main.tasks.database.TaskTreeBuilder;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Konstantin Kostin
 */
public class MainController implements Initializable {

    public MainController() {
    }

    /**
     * ============================
     * | Task list initialization |
     * ============================
     * */

    @FXML private TreeView<TaskItem> tasksList;

    /**
     * Perform application initialization:
     * [*] Build TreeView of tasks
     *
     * @param location {@link javafx.fxml.Initializable}
     * @param resources {@link javafx.fxml.Initializable}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<TaskItem> rootItem = new TaskTreeBuilder().loadTree();
        tasksList.setRoot(rootItem);
        tasksList.setCellFactory((treeView) -> new TaskItemTreeCell());
    }

    /*
     * ==================
     * | Task list edit |
     * ==================
     * */

    /** Current active tree item */
    public static TreeItem<TaskItem> currentTreeItem; // TODO: is set on TaskItemTreeCell. Make more obvious?

    @FXML private ScrollPane detailsPane;
    @FXML private TextFlow detailsNameBlock;
    @FXML private TextFlow detailsPriorityBlock;
    @FXML private TextFlow detailsDeadlineBlock;
    @FXML private TextFlow detailsDescriptionBlock;
    @FXML private Text detailsName;
    @FXML private Text detailsPriority;
    @FXML private Text detailsDeadline;
    @FXML private Text detailsDescription;

    @FXML
    public void onMenuItemClick() {
        if (currentTreeItem != null) {
            TaskItem currentItem = currentTreeItem.getValue();
            detailsPane.setVisible(true);
            detailsName.setText(currentItem.getName() + "\n");
            detailsDescription.setText(currentItem.getDescription() + "\n");
            displayProperties(currentItem);
        }
    }

    private void displayProperties(TaskItem item) {
        if (item instanceof Task) {
            setVisibility(true, detailsPriorityBlock, detailsDeadlineBlock);

            detailsPriority.setText(String.valueOf(((Task) item).getPriority()) + "\n");
            detailsDeadline.setText(((Task) item).getDeadline().toString() + "\n");

        } else if (item instanceof Category) {
            setVisibility(false, detailsPriorityBlock, detailsDeadlineBlock);
        }
    }

    private void setVisibility(boolean isVisible, Node ... items) {
        for (Node item: items) {
            item.setVisible(isVisible);
            item.setManaged(isVisible);
        }
    }


    /**
     * =====================
     * | Task list actions |
     * =====================
     * */

    @FXML private Label statusLabel;

    @FXML
    public void onButtonPressed(Event event) {
        try {
            statusLabel.setText(((Button) event.getSource()).getText());
        } catch (ClassCastException exc) {
            System.err.println("Cast exception: " + exc.getMessage());
        }
    }

    @FXML
    public void onTaskAddition(Event event) {
        onButtonPressed(event);

        TaskItem currentItem = (currentTreeItem != null ? currentTreeItem.getValue() : null);
        TreeItem<TaskItem> additionCell;
        if (currentItem instanceof Category) {
            additionCell = currentTreeItem;
        } else if (currentItem instanceof Task) {
            additionCell = currentTreeItem.getParent();
        } else if (currentItem == null) {
            additionCell = tasksList.getRoot();
        } else {
            throw new IllegalArgumentException("Unknown TaskItem descendant class");
        }

//        TaskItem item = new TaskItem("new");
//        try {
//            ((Category) additionCell.getValue()).addItem(item);
//            additionCell.getChildren().add(new TreeItem<>(item));
//        } catch (DuplicateItemException e) {
//            System.err.println(e.getMessage() + " Name: " + item.getName());
//        }
    }

    @FXML
    public void onEditButton() {
        tasksList.getCellFactory().call(tasksList).startEdit();
    }

    @FXML
    public void onDeleteButton() {
        currentTreeItem.getValue().removeFromDb();
    }


    /**
     * ==============
     * | About menu |
     * ==============
     * */

    public void onAboutMenu() {
        try {
            new AboutWindow().start(new Stage());
        } catch (IOException e) {
            System.err.println("Error while starting about window:");
            System.err.println(e.getMessage());
        }
    }
}
