package scienceWork.objects.machineLearning;

import org.opencv.core.Mat;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;
import org.opencv.ml.TrainData;
import scienceWork.Exceptions.VocabularyNotFoundException;
import scienceWork.MainOperations;
import scienceWork.objects.machineLearning.CommonML.AlgorithmMLImpl;
import scienceWork.objects.machineLearning.mlSettings.SettingsSVM;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_32S;

/**
 * Created by mixa1 on 28.03.2018.
 */
public class SVMInstance extends AlgorithmMLImpl<SVM> {
    private SVM svm;
    private Mat trainingData;
    private int countClusters;
    private int featureID;
    private final int classifierID = 1;
    private String pathSavedData = null;

    private static SVMInstance SVMInstance;

    private SVMInstance(int countClusters, int featureID) {
        this.countClusters = countClusters;
        this.featureID = featureID;

    }

    public SVMInstance() {
    }

    public static SVMInstance getSVMInstance() {
        if (SVMInstance == null) {
            SVMInstance = new SVMInstance();
        }
        return SVMInstance;
    }


    private void initSVM() {
        svm = SVM.create();
        SettingsSVM.setSettings(svm);
    }


    public SVM getInstance() {
        return svm;
    }

    @Override
    public Mat getSupportVectors() {
        return svm.getSupportVectors();
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
    public void setInstance(SVM obj) {
        this.svm = obj;
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
        return pathSavedData;
    }

    @Override
    public void setPath(String path) {
        pathSavedData =path;
    }

    public void setSvm(SVM svm) {
        this.svm = svm;
    }

    @Override
    public int getClassifierId() {
        return classifierID;
    }

    @Override
    public String getType() {
        return "SVM";
    }

    @Override
    public void train() {
        initSVM();
        TrainData trainData = trainingData();
        Mat samples = trainData.getSamples();
        Mat response = trainData.getResponses();
        samples.convertTo(samples, CV_32F);
        response.convertTo(response, CV_32S);
//        svm.train(samples, Ml.ROW_SAMPLE, response);
        svm.trainAuto(samples, Ml.ROW_SAMPLE, response);
//        train(svm, trainingData());
    }

    @Override
    public boolean isTrained() {
        return svm.isTrained();
    }

    @Override
    public float predict(Mat template) {
//        Mat res = new Mat();
//        float predict = svm.predict(template, res, 0);
        //        new MainOperations().showMat(res);
        return svm.predict(template);
    }

    private TrainData trainingData() {
        return super.intiTrainData();
    }

    public String toString() {
        return "SVM:" +
                " \ngetType " + svm.getType() +
                " \ngetKernelType " + svm.getKernelType() +
                " \ngetVarCount " + svm.getVarCount() +
                " \ngetC " + svm.getC() +
                " \ngetCoef0 " + svm.getCoef0() +
                " \ngetDegree " + svm.getDegree() +
                " \ngetGamma " + svm.getGamma() +
                " \ngetNu " + svm.getNu() +
                " \ngetP " + svm.getP() +
                " \ngetTermCriteria " + svm.getTermCriteria().type +
                " \nmaxCount " + svm.getTermCriteria().maxCount +
                " \nepsilon " + svm.getTermCriteria().epsilon;
    }
}
