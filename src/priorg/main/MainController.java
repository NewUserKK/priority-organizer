package priorg.main;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import priorg.about.AboutWindow;
import priorg.main.tasks.Category;
import priorg.main.tasks.TaskItem;
import priorg.main.tasks.TaskParser;
import priorg.main.tasks.TaskItemTreeCell;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Konstantin Kostin
 */
public class MainController implements Initializable {

    @FXML private Label statusLabel;
    @FXML private Button tasksAddButton;
    @FXML private TreeView<TaskItem> tasksList;
//    @FXML private Text detailsText;

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

    public void onButtonPressed(Event event) {

        try {
            statusLabel.setText(((Button) event.getSource()).getText());
        } catch (ClassCastException exc) {
            System.err.println("Cast exception: " + exc.getMessage());
        }
    }

    public void onAboutMenu() {
        try {
            new AboutWindow().start(new Stage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void onTaskAddition(Event e) {
        onButtonPressed(e);
    }

    public void onMenuItemClick(Event e) {
//        detailsText.setText(e.toString());
        System.out.println(e.getSource());
    }
}
