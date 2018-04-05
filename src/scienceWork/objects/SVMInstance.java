package scienceWork.objects;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;
import org.opencv.ml.TrainData;
import scienceWork.objects.picTypesData.BOWVocabulary;

import java.util.Map;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_32S;

/**
 * Created by mixa1 on 28.03.2018.
 */
public class SVMInstance {
    private SVM svm;
    private Mat trainingData;
    private Mat classes;


    private static SVMInstance SVMInstance;

    private SVMInstance() {

    }

    public static SVMInstance getSVMInstance(){
        if(SVMInstance == null){
            SVMInstance = new SVMInstance();
        }
        return SVMInstance;
    }

    private void initSVM(){
        svm = SVM.create();
//        svm.setType(SVMofBOW.C_SVC);
        svm.setKernel(SVM.INTER);
//        svm.setDegree(0.1);
//        svm.setGamma(1);
//        svm.setCoef0(0);
//        svm.setC(1);

//        svm->setType(SVM::C_SVC);
//        svm->setKernel(SVM::LINEAR);
//        svm->setTermCriteria(TermCriteria(TermCriteria::MAX_ITER, 100, 1e-6));

//        svm.setType(SVM.C_SVC);
    ////    svm.setKernel(SVM.RBF);
//        svm.setDegree(1);
//        svm.setGamma(0.1);
//        svm.setType(SVM.NU_SVC);
//        svm.setCoef0(0.00001);
//        svm.setC(10);
//        svm.setNu(0.1);
//        svm.setP(0.000001);
//        svm.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 10000, 0.1));
    }

    public SVM getSvm() {
        return svm;
    }

    public void trainSVM() {
        initSVM();
        trainingData = new Mat();
        byte classNumber = 0;
        classes = new Mat();
        Mat label = new Mat(1, 1, CvType.CV_8UC1);
        if (!BOWVocabulary.vocabularies.isEmpty()) {
            for (Map.Entry<String, Mat> trainData : BOWVocabulary.vocabularies.entrySet()) {
                BOWVocabulary.classesNumbers.add(trainData.getKey());
                trainingData.push_back(trainData.getValue());
                label.put(0, 0, classNumber);
                for (int i = 0; i < trainData.getValue().rows(); i++) {
                    classes.push_back(label);
                }
                classNumber++;
            }

            trainingData.convertTo(trainingData, CV_32F);

            classes.convertTo(classes, CV_32S);
            System.out.println("trainingDataType: " + CvType.typeToString(trainingData.type()));
            System.out.println("classesType: " + CvType.typeToString(classes.type()));
            System.out.println(trainingData.rows() == classes.rows());
            TrainData trainData = TrainData.create(trainingData, Ml.ROW_SAMPLE, classes);
            System.out.println(trainData.getResponseType());

            svm.train(trainData.getSamples(), Ml.ROW_SAMPLE, trainData.getResponses());
//            svm.tr
            System.out.println("SVM finish");
        } else {
            System.out.println("BOWVocabulary.vocabularies Empty");
        }
    }
}
