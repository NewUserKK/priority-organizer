package priorg.main;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import priorg.about.AboutWindow;
import priorg.main.tasks.*;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Konstantin Kostin
 */
public class MainController implements Initializable {


    /**
     * ============================
     * | Task list initialization |
     * ============================
     * */

    @FXML private TreeView<TaskItem> tasksList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Category rootCategory = new Category("root");
        new TaskParser(rootCategory).parse(Config.TASK_DB_PATH.toString());

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

    public static TaskItem currentItem;
    @FXML private TextFlow detailsTextFlow;
    @FXML private Text taskItemName;
    @FXML private Text taskItemPriority;
    @FXML private Text taskItemPriorityLabel;
    @FXML private Text taskItemDeadline;
    @FXML private Text taskItemDeadlineLabel;
    @FXML private Text taskItemDescription;

    public void onMenuItemClick() {
        // TODO: change to multiple panels (easier to change visibility)
        if (currentItem != null) {
            detailsTextFlow.setVisible(true);
            taskItemName.setText(currentItem.getName() + "\n");
            taskItemDescription.setText(currentItem.getDescription() + "\n");
            displayProperties(currentItem);
        }
    }

    private void displayProperties(TaskItem item) {
        if (item instanceof Task) {
            taskItemPriorityLabel.setVisible(true);
            taskItemPriority.setVisible(true);
            taskItemPriority.setText(String.valueOf(((Task) currentItem).getPriority()) + "\n");

            taskItemDeadlineLabel.setVisible(true);
            taskItemDeadline.setVisible(true);
            taskItemDeadline.setText(((Task) currentItem).getDeadline().toString() + "\n");

        } else if (item instanceof Category) {
            taskItemPriorityLabel.setVisible(false);
            taskItemPriority.setVisible(false);

            taskItemDeadlineLabel.setVisible(false);
            taskItemDeadline.setVisible(false);
        }
    }


    /**
     * =====================
     * | Task list buttons |
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

    public void onTaskAddition(Event e) {
        onButtonPressed(e);
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
