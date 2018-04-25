package scienceWork.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scienceWork.Main;
import scienceWork.dataBase.VocabularyDB;
import scienceWork.objects.FeatureTypes;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.data.Vocabulary;

import java.net.URL;
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
        textField.setText("Type: " + FeatureTypes.getLabel(Settings.getMethodKP()) + "; Size: " + Settings.getCountClusters());
        updateChoiceBox();
    }

    private void updateChoiceBox() {
        vocabularies = VocabularyDB.getInstance().loadVocabulary(
                Settings.getCountClusters(),
                FeatureTypes.getFeatureId(Settings.getMethodKP()));
        vocabulariesCB.setItems(convertListsToObservableList(vocabularies));
    }

    @FXML
    private void loadSelectedVocabulary() {
        boolean error = false;
        int indx = vocabulariesCB.getSelectionModel().getSelectedIndex();
        if (indx > 0){

            BOWVocabulary.vocabulary = vocabularies.get(indx-1);
        }
        showMessage("Success");
        dialogStage.close();
    }

    @FXML
    public void cancel(){
        dialogStage.close();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("result");
        alert.setHeaderText(message);
        alert.setContentText(":-|");

        alert.showAndWait();
    }

    private ObservableList<String> convertListsToObservableList(List<Vocabulary> vocabularies) {
        ObservableList<String> observableListPicures = FXCollections.observableArrayList();
        for (Vocabulary vocabulary : vocabularies) {
            observableListPicures.add(vocabulary.toString());
        }

        return observableListPicures;
    }
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

}