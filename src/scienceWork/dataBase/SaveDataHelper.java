package scienceWork.dataBase;

import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.dataBase.WorkerDB;
import scienceWork.objects.machineLearning.CommonML.AlgorithmML;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.data.Vocabulary;

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
                progress.addMessage("Classifier saved: " + classifier.toString()+"\n");
            } else {
                progress.addMessage("Process saving of classifier data to database failed;\n");
            }
        }
        return saved;
    }

    public static boolean saveVocabulary(Vocabulary vocabulary, Progress progress) {
        boolean saved;
        if (Settings.SAVE_DATA) {
            saved = WorkerDB.getInstance().saveVocabulary(vocabulary);
            if (saved) {
                progress.addMessage("Vocabulary saved: " + vocabulary.toString()+"\n");
            } else {
                progress.addMessage("Process saving of vocabulary data to database failed: " + vocabulary.toString()+"\n");
            }
        }
        return saved;
    }


}
