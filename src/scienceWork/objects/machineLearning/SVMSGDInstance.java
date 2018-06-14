package scienceWork.objects.machineLearning;

import org.opencv.core.Mat;
import org.opencv.ml.Ml;
import org.opencv.ml.SVMSGD;
import org.opencv.ml.TrainData;
import scienceWork.objects.machineLearning.CommonML.AlgorithmMLImpl;
import scienceWork.objects.machineLearning.mlSettings.SettingsSVMSGD;

import static org.opencv.core.CvType.CV_32FC1;


public class SVMSGDInstance extends AlgorithmMLImpl<SVMSGD> {
    private SVMSGD svmsgd;
    private Mat trainingData;
    private int countClusters;
    private int featureID;
    private final int classifierID = 1;


    private static SVMSGDInstance svmsgdInstance;

    private SVMSGDInstance(int countClusters, int featureID) {
        this.countClusters = countClusters;
        this.featureID = featureID;

    }

    public SVMSGDInstance() {
    }

    public static SVMSGDInstance getSVMSGDInstance() {
        if (svmsgdInstance == null) {
            svmsgdInstance = new SVMSGDInstance();
        }
        return svmsgdInstance;
    }


    private void initSVMSGD() {
        svmsgd = SVMSGD.create();
        SettingsSVMSGD.setSettings(svmsgd);
    }


    public SVMSGD getInstance() {
        return svmsgd;
    }

    @Override
    public Mat getSupportVectors() {
        return svmsgd.getWeights();
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
    public void setInstance(SVMSGD obj) {
        this.svmsgd = obj;
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

    public void setSvmsgd(SVMSGD svmsgd) {
        this.svmsgd = svmsgd;
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
        initSVMSGD();
        train(svmsgd, trainingData());


//        svmsgd.train(trainData);


    }

    @Override
    public boolean isTrained() {
        return svmsgd.isTrained();
    }

    @Override
    public float predict(Mat template) {
        return svmsgd.predict(template);
    }

    private TrainData trainingData() {
        return super.intiTrainData();
    }

    public String toString() {
        return "SVMSGD:" +
                " getTermCriteria " + svmsgd.getTermCriteria().type +
                " \nmaxCount " + svmsgd.getTermCriteria().maxCount +
                " \nepsilon " + svmsgd.getTermCriteria().epsilon;
    }
}
