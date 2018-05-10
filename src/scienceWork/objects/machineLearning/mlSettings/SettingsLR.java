package scienceWork.objects.machineLearning.mlSettings;

import org.opencv.core.TermCriteria;
import org.opencv.ml.LogisticRegression;

public class SettingsLR {

    public static void setSettings(LogisticRegression logisticRegression){
//        logisticRegression.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 1000, 0.1));
        logisticRegression.setIterations(5000);
        logisticRegression.setLearningRate(0.000003);
        logisticRegression.setRegularization(1);
        logisticRegression.setTrainMethod(LogisticRegression.BATCH);
//        logisticRegression.setMiniBatchSize(1000);
//        logisticRegression.setMiniBatchSize();
    }

}
