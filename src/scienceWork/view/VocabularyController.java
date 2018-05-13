package scienceWork.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scienceWork.FxWorker.FxHelper;
import scienceWork.Main;
import scienceWork.dataBase.WorkerDB;
import scienceWork.objects.constants.FeatureTypes;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.data.Vocabulary;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class VocabularyController implements Initializable {
    private Stage dialogStage;
    private List<Vocabulary> vocabularies;
    private Main mainApp;
    @FXML
    private ChoiceBox vocabulariesCB;
    @FXML
    private Button loadBT;
    @FXML
    private Button deleteBT;
    @FXML
    private Button cancelBT;
    @FXML
    private TextField textField;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textField.setText("Type: " + FeatureTypes.getLabel(Settings.getMethodKP()) + "; Size: " + Settings.getCountWords());
        updateChoiceBox();
    }

    /* todo no need load all mats, only text */
    private void updateChoiceBox() {
        try {
            vocabularies = WorkerDB.getInstance().loadVocabulary(
                    Settings.getCountWords(),
                    FeatureTypes.getFeatureId(Settings.getMethodKP()));
            vocabulariesCB.setItems(FxHelper.convertListToObservableList(vocabularies));

//            if(BOWVocabulary.vocabulary != null) {
//                vocabulariesCB.setValue(BOWVocabulary.vocabulary.toString());
//            }
        } catch (SQLException | ClassNotFoundException e){
            FxHelper.showMessage(
                    "Data Base error",
                    "Failed to connect to Data Base",
                    "Please check DB settings or status connection",
                    Alert.AlertType.ERROR,
                    new Main());
            dialogStage.close();
        }
    }

    @FXML
    private void loadSelectedVocabulary() {
        int indx = vocabulariesCB.getSelectionModel().getSelectedIndex();

        if (indx >= 0){
            BOWVocabulary.vocabulary = vocabularies.get(indx);
            FxHelper.showMessage(
                    "Load Vocabulary",
                    "Vocabulary was loaded",
                    BOWVocabulary.vocabulary.toString(),
                    Alert.AlertType.INFORMATION,
                    mainApp);
        }

        dialogStage.close();
    }

    @FXML
    public void cancel(){
        dialogStage.close();
    }


    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

}
