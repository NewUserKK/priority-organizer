package priorg.about;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author Konstantin Kostin
 */
public class AboutController {

    @FXML
    Button closeButton;

    public void onClose() {
        Stage buttonStage = (Stage) closeButton.getScene().getWindow();
        buttonStage.close();
    }

}
