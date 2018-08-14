package priorg.buttons;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AddEditController {
    @FXML
    Button closeAddEditButton;

    public void onClose() {
        Stage buttonStage = (Stage) closeAddEditButton.getScene().getWindow();
        buttonStage.close();
    }
}
