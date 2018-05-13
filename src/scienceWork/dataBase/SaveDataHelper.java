package scienceWork.dataBase;

import javafx.scene.control.Alert;
import scienceWork.FxWorker.FxHelper;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.Main;
import scienceWork.dataBase.WorkerDB;
import scienceWork.objects.machineLearning.CommonML.AlgorithmML;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.data.Vocabulary;

import java.sql.SQLException;

public class SaveDataHelper {

    public static boolean saveClassifier(AlgorithmML classifier, Progress progress) {
//        SVM svm = (SVM)classifier.getInstance();
//        String s = String.valueOf(svm);
//        svm.save("E:\\YandexDisk\\JavaProject\\SDiplom\\123.xml");
//        svm.getClassWeights().size();
//        svm.getSupportVectors().size();
//        svm.getUncompressedSupportVectors().size();

//        return   WorkerDB.getInstance().saveClassifier(classifier);

        boolean saved = false;
        if (Settings.SAVE_DATA) {
//            saved = WorkerDB.getInstance().saveClassifier(classifier);
            if (saved) {
                progress.addMessage("Classifier saved: " + classifier.toString() + "\n");
            } else {
                progress.addMessage("Process saving of classifier data to database failed;\n");
            }
        }
        return saved;
    }

    public static boolean saveVocabulary(Vocabulary vocabulary, Progress progress) {
        boolean saved = false;
        if (Settings.SAVE_DATA) {
            try {
                saved = WorkerDB.getInstance().saveVocabulary(vocabulary);
                if (saved) {
                    progress.addMessage("Vocabulary saved: " + vocabulary.toString() + "\n");
                } else {
                    progress.addMessage("Process saving of vocabulary data to database failed: " + vocabulary.toString() + "\n");
                }
            } catch (SQLException | ClassNotFoundException e) {
                FxHelper.showMessage(
                        "Data Base error",
                        "Failed to connect to Data Base",
                        "Please check DB settings or status connection",
                        Alert.AlertType.ERROR,
                        new Main());
            }
        }
        return saved;
    }


}
