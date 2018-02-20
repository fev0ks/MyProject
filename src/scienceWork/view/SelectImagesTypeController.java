package scienceWork.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class SelectImagesTypeController {
    private Stage dialogStage;
    private String type;
    @FXML
    private TextField typeImagesTF;

    @FXML
    private void nextStep(){
        type = typeImagesTF.getText();
        if (type == null || type.trim().equals("") || type.equals("Select type")) {
            typeImagesTF.setPromptText("Select type");
        } else {
            dialogStage.close();
        }
    }

    public String isOkClicked() {
        return type;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
