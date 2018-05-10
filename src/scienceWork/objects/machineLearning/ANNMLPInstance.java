package scienceWork.objects.machineLearning;

import org.opencv.core.Mat;
import org.opencv.ml.ANN_MLP;
import org.opencv.ml.TrainData;
import scienceWork.objects.machineLearning.CommonML.AlgorithmMLImpl;
import scienceWork.objects.machineLearning.mlSettings.SettingsANNMLP;

public class ANNMLPInstance extends AlgorithmMLImpl<ANN_MLP> {
    private ANN_MLP annMlp;
    private int countClusters;
    private int featureID;
    private final int classifierID = 1;


    private static ANNMLPInstance annmlpInstance;

    private ANNMLPInstance(int countClusters, int featureID) {
        this.countClusters = countClusters;
        this.featureID = featureID;

    }

    public ANNMLPInstance() {
    }

    public static ANNMLPInstance getANNMLPInstance() {
        if (annmlpInstance == null) {
            annmlpInstance = new ANNMLPInstance();
        }
        return annmlpInstance;
    }


    private void ANNMLP() {
        annMlp = ANN_MLP.create();
        SettingsANNMLP.setSettings(annMlp);
    }


    public ANN_MLP getInstance() {
        return annMlp;
    }

    @Override
    public Mat getSupportVectors() {
        return annMlp.getLayerSizes();
    }

    @Override
    public int getFeatureID() {
        return featureID;
    }

    @Override
    public int getCountClusters() {
        return countClusters;
    }

    @Override
    public void setInstance(ANN_MLP obj) {
        this.annMlp = obj;
    }

    @Override
    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }

    @Override
    public void setCountClusters(int countClusters) {
        this.countClusters = countClusters;
    }

    public void setANNMLP(ANN_MLP svmsgd) {
        this.annMlp = svmsgd;
    }

    @Override
    public int getClassifierId() {
        return classifierID;
    }

    @Override
    public void train() {
        ANNMLP();
        train(annMlp, trainingData());
    }

    @Override
    public float predict(Mat template) {
        return annMlp.predict(template);
    }

    private TrainData trainingData() {
        return super.intiTrainData();
    }

    public String toString() {
        return "ANN MLP:" +
                " getTermCriteria " + annMlp.getTermCriteria().type +
                " \nmaxCount " + annMlp.getTermCriteria().maxCount +
                " \nepsilon " + annMlp.getTermCriteria().epsilon;
    }
}
