package priorg.main;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import priorg.about.AboutWindow;
import priorg.main.tasks.Category;
import priorg.main.tasks.TaskItem;
import priorg.main.tasks.TaskParser;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Category rootCategory = new Category("root");
        new TaskParser(rootCategory).parse("src/priorg/main/tasks/db_sample.txt");

        TreeItem<TaskItem> root = new TreeItem<>(rootCategory);
        tasksList.setRoot(root);
        for (TaskItem t: rootCategory.getSubItems()) {
            dfs(t, root);
        }
    }

    private void dfs(TaskItem node, TreeItem<TaskItem> root) {
        TreeItem<TaskItem> nodeTreeItem = new TreeItem<>(node);
        root.getChildren().add(nodeTreeItem);
        if (node instanceof Category) {
            for (TaskItem item: ((Category) node).getSubItems()) {
                dfs(item, nodeTreeItem);
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
}
