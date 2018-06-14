package scienceWork.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import scienceWork.FxWorker.FxHelper;
import scienceWork.Main;
import scienceWork.objects.constants.Constants;
import scienceWork.objects.constants.Settings;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class OptionsController implements Initializable {
    private Stage dialogStage;
    //    private int settingNumber;
//    private final int THREAD_NUMBER = 1;
//    private final int CLUSTERS_NUMBER = 2;
//    private final int METHOD_NUMBER = 3;
    private final int SIZE_ITEM_IN_LISTVIEW = 22;
    @FXML
    private TextField countOfClustersTF;
    @FXML
    private TextField resizeImgTF;
    @FXML
    private ChoiceBox<String> methodsChoiceB;
    @FXML
    private CheckBox saveBOWCB;
    @FXML
    private CheckBox saveClassifierCB;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        messageTF.setVisible(false);
        countOfClustersTF.setText(Settings.getCountWords() + "");
        resizeImgTF.setText(Settings.getScaleImageRatio() + "");
        saveBOWCB.setSelected(Settings.isSaveBOW());
        saveClassifierCB.setSelected(Settings.isSaveClassifier());

        methodsChoiceB.setItems(FXCollections.observableArrayList(
                new LinkedList<>(Arrays.asList(Constants.AKAZE, Constants.ORB, Constants.BRISK))));
        methodsChoiceB.getSelectionModel().select(Settings.getMethod());

    }

    @FXML
    private void resetToDefault() {
        Settings.getInstance().resetSettingsToDefault();
    }

    @FXML
    private void saveOptions() {
        boolean error = false;
        try {
            Settings.setCountOfClusters(Integer.parseInt(countOfClustersTF.getText()));
            Settings.setScaleImageRatio(Integer.parseInt(resizeImgTF.getText()));
        } catch (Exception e) {
            error = true;
            FxHelper.showMessage("Error", "Number Format Exception", "Set only integer", Alert.AlertType.ERROR, new Main());
            System.out.println("saveOptions: NumberFormatException");
        }

        Settings.setSaveBOW(saveBOWCB.isSelected());
        Settings.setSaveClassifier(saveClassifierCB.isSelected());

        if (methodsChoiceB.getSelectionModel().getSelectedItem() != null)
            switch (methodsChoiceB.getSelectionModel().getSelectedItem()) {
                case Constants.AKAZE:
                    Settings.setMethodKP(FeatureDetector.AKAZE);
                    Settings.setMethodDescr(DescriptorExtractor.AKAZE);
                    Settings.setMethod(Constants.AKAZE);
                    break;
                case Constants.BRISK:
                    Settings.setMethodKP(FeatureDetector.BRISK);
                    Settings.setMethodDescr(DescriptorExtractor.BRISK);
                    Settings.setMethod(Constants.BRISK);
                    break;
                case Constants.ORB:
                    Settings.setMethodKP(FeatureDetector.ORB);
                    Settings.setMethodDescr(DescriptorExtractor.ORB);
                    Settings.setMethod(Constants.ORB);
                    break;
            }
        if (!error) {
            dialogStage.close();
        }
    }


}
