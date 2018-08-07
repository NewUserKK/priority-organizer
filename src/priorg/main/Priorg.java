package priorg.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import priorg.main.tasks.database.CsvCategoryHandler;
import priorg.main.tasks.database.CsvHandler;
import priorg.main.tasks.database.CsvTaskHandler;

import java.io.IOException;

/**
 * @author Konstantin Kostin
 */
public class Priorg extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("priorg.fxml"));
        primaryStage.setTitle("Priorg");
        primaryStage.setScene(new Scene(root));

        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        CsvCategoryHandler.getInstance().close();
        CsvTaskHandler.getInstance().close();
    }
}
