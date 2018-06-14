package scienceWork.objects.machineLearning;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.ml.ANN_MLP;
import org.opencv.ml.Ml;
import org.opencv.ml.TrainData;
import scienceWork.MainOperations;
import scienceWork.algorithms.bow.VocabularyTools;
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

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void setPath(String path) {

    }

    public void setANNMLP(ANN_MLP svmsgd) {
        this.annMlp = svmsgd;
    }

    @Override
    public int getClassifierId() {
        return classifierID;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void train() {
        initANNMLP();
        TrainData trainData = trainingData();
        Mat samples = trainData.getSamples();
        Mat response = trainData.getResponses();
        samples.convertTo(samples, CV_32FC1);
        response.convertTo(response, CvType.CV_32F);
//        new MainOperations().showMat(annMlp.getLayerSizes());
//        new MainOperations().showMat(response);

        annMlp.train(samples, Ml.ROW_SAMPLE, response);

//        train(annMlp, TrainData.create(samples, Ml.ROW_SAMPLE, response));
//        new MainOperations().showMat(annMlp.getLayerSizes());
        new MainOperations().showMat(annMlp.getWeights(annMlp.getLayerSizes().rows()));
//        System.out.println("annMlp.getLayerSizes().rows() " + annMlp.getLayerSizes().rows());
//        System.out.println("getWeights " + annMlp.getWeights(annMlp.getLayerSizes().rows()).cols());
//        new MainOperations().showMat(annMlp.getWeights(5));
//        train(annMlp, trainingData());
    }

    public boolean isTrained() {
        return annMlp.isTrained();
    }

    @Override
    public float predict(Mat template) {
        Mat res = new Mat();
//        float predict = annMlp.predict(template);
        float predict =  annMlp.predict(template, res, 4);
//                System.out.println(((int)data.get(row, 0)[0]) +" - "+((int)pr));
        new MainOperations().showMat(res);
//        System.out.print(predict + " ");
        return predict;
    }

    private TrainData trainingData() {
        Mat trainingData = new Mat();
        int classNumber = 0;
        Mat classes = new Mat();

//        Mat label = new Mat(1,1, CvType.CV_32F);
        for (Map.Entry<String, Mat> trainData : BOWVocabulary.vocabularies.entrySet()) {

            Mat label = Mat.zeros(1, BOWVocabulary.vocabularies.size(), CvType.CV_32F);
            System.out.println("Label: " + trainData.getKey());
            BOWVocabulary.classesNumbers.add(trainData.getKey());
            trainingData.push_back(trainData.getValue());

            label.put(0, classNumber, classNumber + 1);

            for (int i = 0; i < trainData.getValue().rows(); i++) {
                classes.push_back(label);
            }
            classNumber++;
        }

        trainingData.convertTo(trainingData, CvType.CV_32F);
        classes.convertTo(classes, CvType.CV_32F);
        return TrainData.create(trainingData, Ml.ROW_SAMPLE, classes);
    }

    public String toString() {
        return "ANN MLP:" +
                " getTermCriteria " + annMlp.getTermCriteria().type +
                " \nmaxCount " + annMlp.getTermCriteria().maxCount +
                " \nepsilon " + annMlp.getTermCriteria().epsilon;
    }
}
