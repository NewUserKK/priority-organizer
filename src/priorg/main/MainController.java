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
        if (currentItem != null) {
            detailsPane.setVisible(true);
            detailsName.setText(currentItem.getName() + "\n");
            detailsDescription.setText(currentItem.getDescription() + "\n");
            displayProperties(currentItem);
        }
    }

    private void displayProperties(TaskItem item) {
        if (item instanceof Task) {
            setVisibility(true, detailsPriorityBlock, detailsDeadlineBlock);

            detailsPriority.setText(String.valueOf(((Task) currentItem).getPriority()) + "\n");
            detailsDeadline.setText(((Task) currentItem).getDeadline().toString() + "\n");

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
