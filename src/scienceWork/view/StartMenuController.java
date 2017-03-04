package scienceWork.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import scienceWork.Main;
import scienceWork.WorkFolder;

import java.io.File;

public class StartMenuController {
    private Main mainApp;
    private WorkFolder workFolder;
    private Stage dialogStage;
    private File dir;
    @FXML
    private TextField dirPathTF;
    @FXML
    private Button loadDirB;
    @FXML
    private Button nextB;

    public void setMainApp(Main mainApp) {
        nextB.setDisable(true);
        this.mainApp = mainApp;
    }

    @FXML
    private void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        dir = directoryChooser.showDialog(mainApp.getPrimaryStage());
        if (dir == null) {
            dirPathTF.setText("No Directory selected");
        } else {
            dirPathTF.setText(dir.getAbsolutePath());
            nextB.setDisable(false);
        }
    }
    @FXML
    private void nextStep(){

        dialogStage.close();
    }
    public File isOkClicked() {
        return dir;
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}