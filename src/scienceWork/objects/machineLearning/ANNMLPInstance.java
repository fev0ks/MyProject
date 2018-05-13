package scienceWork.objects.machineLearning;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.ml.ANN_MLP;
import org.opencv.ml.Ml;
import org.opencv.ml.TrainData;
import scienceWork.MainOperations;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.machineLearning.CommonML.AlgorithmMLImpl;
import scienceWork.objects.machineLearning.mlSettings.SettingsANNMLP;

import java.util.Map;

import static org.opencv.core.CvType.CV_32FC1;
import static org.opencv.core.CvType.CV_32S;

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


    private void initANNMLP() {
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
        initANNMLP();
        TrainData trainData = trainingData();
        Mat samples = trainData.getSamples();
        Mat response = trainData.getResponses();
        samples.convertTo(samples, CV_32FC1);
        response.convertTo(response, CvType.CV_32F);
        new MainOperations().showMat(annMlp.getLayerSizes());
        new MainOperations().showMat(response);
        train(annMlp, TrainData.create(samples, Ml.ROW_SAMPLE, response));
//        new MainOperations().showMat(annMlp.getWeights(5));
//        train(annMlp, trainingData());
    }

    @Override
    public float predict(Mat template) {
        return annMlp.predict(template);
    }

    private TrainData trainingData() {
        Mat trainingData = new Mat();
        int classNumber = 0;
        Mat classes = new Mat();
        Mat label = new Mat(1, BOWVocabulary.vocabularies.size(), CvType.CV_32F);
        for (Map.Entry<String, Mat> trainData : BOWVocabulary.vocabularies.entrySet()) {
            BOWVocabulary.classesNumbers.add(trainData.getKey());
            trainingData.push_back(trainData.getValue());
            for(int i =0; i < label.cols(); i++){
                if(i != classNumber){
                    label.put(0, i, 0);
                } else {
                    label.put(0, classNumber, classNumber);
                }
            }
//            label.put(0, label.cols()-2, classNumber);
//            label.put(0, label.cols()-1, classNumber);
            for (int i = 0; i < trainData.getValue().rows(); i++) {
                classes.push_back(label);
            }
            classNumber++;
        }

//            trainingData.convertTo(trainingData, CV_32F);
        classes.convertTo(classes, CV_32S);
        return TrainData.create(trainingData, Ml.ROW_SAMPLE, classes);
//        return super.intiTrainData();
    }

    public String toString() {
        return "ANN MLP:" +
                " getTermCriteria " + annMlp.getTermCriteria().type +
                " \nmaxCount " + annMlp.getTermCriteria().maxCount +
                " \nepsilon " + annMlp.getTermCriteria().epsilon;
    }
}
