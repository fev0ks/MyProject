package scienceWork.algorithms.bow.SVMHelper;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.objects.SVMInstance;

/**
 * Created by mixa1 on 17.03.2018.
 */
public class SVMofBOW {
    private Progress progress;
    private org.opencv.ml.SVM svm;
    private Mat trainingData;
    private Mat classes;


    public SVMofBOW(Progress progress) {
        this.progress = progress;
        svm = SVMInstance.getSVMInstance().getSvm();
    }

    private void trainSVM() {

//        trainingData = new Mat();
//        byte classNumber = 0;
//        classes = new Mat();
//        Mat label = new Mat(1, 1, CvType.CV_8UC1);
//
//        for (Map.Entry<String, Mat> trainData : BOWVocabulary.vocabularies.entrySet()) {
//            BOWVocabulary.classesNumbers.add(trainData.getKey());
//            trainingData.push_back(trainData.getValue());
//            label.put(0, 0, classNumber);
//            for (int i = 0; i < trainData.getValue().rows(); i++) {
//                classes.push_back(label);
//            }
//            classNumber++;
//        }
//
//        trainingData.convertTo(trainingData, CV_32F);
//
//        classes.convertTo(classes, CV_32S);
//        System.out.println("trainingDataType: " + CvType.typeToString(trainingData.type()));
//        System.out.println("classesType: " + CvType.typeToString(classes.type()));
//        System.out.println(trainingData.rows() == classes.rows());
//        TrainData trainData = TrainData.create(trainingData, Ml.ROW_SAMPLE, classes);
//        System.out.println(trainData.getResponseType());
//
//        svm.train(trainData.getSamples(), Ml.ROW_SAMPLE, trainData.getResponses());
    }

}
