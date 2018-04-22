package scienceWork.objects;

import org.opencv.core.Mat;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;
import org.opencv.ml.TrainData;
import scienceWork.Exceptions.VocabularyNotFoundException;
import scienceWork.objects.CommonML.AlgorithmMLImpl;
import scienceWork.objects.constants.SettingsSVM;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_32FC1;
import static org.opencv.core.CvType.CV_32S;

/**
 * Created by mixa1 on 28.03.2018.
 */
public class SVMInstance extends AlgorithmMLImpl<SVM> {
    private SVM svm;
    private Mat trainingData;
    private Mat classes;


    private static SVMInstance SVMInstance;

    private SVMInstance() {}

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

    public void train() {
        initSVM();
//        trainingData = new Mat();
//        byte classNumber = 0;
//        classes = new Mat();
//        Mat label = new Mat(1, 1, CvType.CV_8UC1);
//        if (!BOWVocabulary.vocabularies.isEmpty()) {
//            for (Map.Entry<String, Mat> trainData : BOWVocabulary.vocabularies.entrySet()) {
//                BOWVocabulary.classesNumbers.add(trainData.getKey());
//                trainingData.push_back(trainData.getValue());
//                label.put(0, 0, classNumber);
//                for (int i = 0; i < trainData.getValue().rows(); i++) {
//                    classes.push_back(label);
//                }
//                classNumber++;
//            }
//
//            trainingData.convertTo(trainingData, CV_32F);
//
//            classes.convertTo(classes, CV_32S);
//            System.out.println("trainingDataType: " + CvType.typeToString(trainingData.type()));
//            System.out.println("classesType: " + CvType.typeToString(classes.type()));
//            System.out.println(trainingData.rows() == classes.rows());
//            TrainData trainData = TrainData.create(trainingData, Ml.ROW_SAMPLE, classes);

        try {
            TrainData trainData = trainingData();
            Mat samples = trainData.getSamples();
            Mat response = trainData.getResponses();
            samples.convertTo(samples, CV_32F);
            response.convertTo(response, CV_32S);
            svm.train(samples, Ml.ROW_SAMPLE, response);
            System.out.println("SVM finish");
        } catch (VocabularyNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public float predict(Mat template) {
        return svm.predict(template);
    }

    private TrainData trainingData() throws VocabularyNotFoundException {
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
