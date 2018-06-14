package scienceWork.Exceptions;

import javafx.scene.control.Alert;
import scienceWork.FxWorker.FxHelper;
import scienceWork.Main;
import scienceWork.objects.Picture;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.machineLearning.CommonML.AlgorithmML;

import java.util.List;

public class DataStateChecker {
    private Main mainStage;

    public DataStateChecker(Main mainStage) {
        this.mainStage = mainStage;
    }

    public  boolean checkExistTrainData() {
        boolean exist = true;
        if (BOWVocabulary.vocabularies.isEmpty()) {
            FxHelper.showMessage("Warning", "Train data is empty!", "Please create", Alert.AlertType.WARNING, mainStage);
            exist = false;
        }
        return exist;
    }

    public  boolean checkExistClassifierInstance(AlgorithmML algorithmML) {
        boolean exist = true;
        if (algorithmML == null) {
            FxHelper.showMessage("Warning", "Classifier Instance is not exist!", "Please create", Alert.AlertType.WARNING, mainStage);
            exist = false;
        }
        return exist;
    }

    public  boolean checkLoadedPictures(List<List<Picture>> pictureLists) {
        boolean exist = true;
        if (pictureLists == null) {
            FxHelper.showMessage("Warning", "Pictures is not loaded", "Please wait loading or choose folder", Alert.AlertType.WARNING, mainStage);
            exist = false;
        }
        return exist;
    }

    public  boolean checkTrainedClassifier(AlgorithmML classifier) {
        boolean exist = true;
        if (!classifier.isTrained()) {
            FxHelper.showMessage("Warning", "Classifier is not trained", "Please train the classifier or wait for it to finish training", Alert.AlertType.WARNING, mainStage);
            exist = false;
        }
        return exist;
    }

    public boolean checkExistVocabulary() {
        boolean exist = true;
        if (BOWVocabulary.vocabulary.getMat() == null) {
            FxHelper.showMessage("Warning", "Vocabulary is empty!", "Please load or create", Alert.AlertType.WARNING, new Main());
            exist = false;
        }
        return exist;
    }

}
