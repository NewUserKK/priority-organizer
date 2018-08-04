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
import priorg.main.tasks.*;
import priorg.main.tasks.database.TasksDatabase;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Konstantin Kostin
 */
public class MainController implements Initializable {

    private TasksDatabase db;

    public MainController() {
        db = TasksDatabase.getInstance();
    }

    /**
     * ============================
     * | Task list initialization |
     * ============================
     * */

    @FXML private TreeView<TaskItem> tasksList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Category rootCategory = new Category("root", true);
        db.loadTree(rootCategory);

        TreeItem<TaskItem> rootItem = new TreeItem<>(rootCategory);
        tasksList.setRoot(rootItem);
        for (TaskItem t: rootCategory.getSubItems()) {
            buildTree(t, rootItem);
        }

        tasksList.setCellFactory((treeView) -> new TaskItemTreeCell());
    }

    private void buildTree(TaskItem node, TreeItem<TaskItem> rootItem) {
        TreeItem<TaskItem> nodeTreeItem = new TreeItem<>(node);
        rootItem.getChildren().add(nodeTreeItem);
        if (node instanceof Category) {
            for (TaskItem item: ((Category) node).getSubItems()) {
                buildTree(item, nodeTreeItem);
            }
        }
    }


    /**
     * ==================
     * | Task list edit |
     * ==================
     * */

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

    public void onButtonPressed(Event event) {
        try {
            statusLabel.setText(((Button) event.getSource()).getText());
        } catch (ClassCastException exc) {
            System.err.println("Cast exception: " + exc.getMessage());
        }
    }

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

        TaskItem item = new TaskItem("new");
        try {
            ((Category) additionCell.getValue()).addItem(item);
            additionCell.getChildren().add(new TreeItem<>(item));
        } catch (DuplicateNameException e) {
            System.err.println(e.getMessage() + " Name: " + item.getName());
        }
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
            System.err.println(e.getMessage());
        }
    }
}
