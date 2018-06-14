package scienceWork.objects.machineLearning;

import org.opencv.core.Mat;
import org.opencv.ml.LogisticRegression;
import org.opencv.ml.Ml;
import org.opencv.ml.TrainData;
import scienceWork.Exceptions.VocabularyNotFoundException;
import scienceWork.objects.machineLearning.CommonML.AlgorithmMLImpl;
import scienceWork.objects.machineLearning.mlSettings.SettingsLR;

import java.util.Arrays;

import static org.opencv.core.CvType.CV_32FC1;

public class LRInstance extends AlgorithmMLImpl<LogisticRegression> {
    private LogisticRegression logisticRegression;
    private int countClusters;
    private int featureID;
    private final int classifierID = 2;
    private static LRInstance LRInstance;

    private LRInstance() {

    }

    public static LRInstance getLRInstance() {
        if (LRInstance == null) {
            LRInstance = new LRInstance();
        }
        return LRInstance;
    }

    private void initLR() {
        logisticRegression = LogisticRegression.create();
        SettingsLR.setSettings(logisticRegression);
    }

    public LogisticRegression getInstance() {
        return logisticRegression;
    }

    @Override
    public Mat getSupportVectors() {
        return logisticRegression.get_learnt_thetas();
    }

    @Override
    public int getFeatureID() {
        return featureID;
    }

    @Override
    public int getCountClusters() {
        return 0;
    }

    @Override
    public void setInstance(LogisticRegression obj) {
        this.logisticRegression = obj;
    }

    @Override
    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }

    @Override
    public void setCountClusters(int countClusters) {

    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void setPath(String path) {

    }

    @Override
    public int getClassifierId() {
        return 0;
    }

    @Override
    public String getType() {
        return null;
    }

    public void train() {
        initLR();
        TrainData trainData = trainingData();
        Mat samples = trainData.getSamples();
        Mat response = trainData.getResponses();
        samples.convertTo(samples, CV_32FC1);
        response.convertTo(response, CV_32FC1);

        logisticRegression.train(samples, Ml.ROW_SAMPLE, response);
//        train(logisticRegression, TrainData.create(samples, Ml.ROW_SAMPLE, response));
//
    }

    @Override
    public boolean isTrained() {
        return logisticRegression.isTrained();
    }

    @Override
    public float predict(Mat template) {
        return logisticRegression.predict(template);
    }

    private TrainData trainingData() {
        return super.intiTrainData();
    }

    public String toString() {
        return "LogisticRegression:" +
                " \ngetTermCriteria " + logisticRegression.getTermCriteria() +
                " \ngetIterations " + logisticRegression.getIterations() +
                " \ngetLearningRate " + logisticRegression.getLearningRate() +
                " \ngetMiniBatchSize " + logisticRegression.getMiniBatchSize() +
                " \ngetRegularization " + logisticRegression.getRegularization() +
                " \ngetTrainMethod " + logisticRegression.getTrainMethod();
    }


}
