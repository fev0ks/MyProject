package scienceWork.objects;

import org.opencv.ml.LogisticRegression;
import org.opencv.ml.Ml;
import org.opencv.ml.TrainData;
import scienceWork.Exceptions.VocabularyNotFoundException;
import scienceWork.objects.CommonML.AlgorithmMLImpl;
import scienceWork.objects.constants.SettingsLR;

public class LRInstance extends AlgorithmMLImpl<LogisticRegression> {
    private LogisticRegression logisticRegression;

    private static LRInstance LRInstance;

    private LRInstance() {

    }

    public static LRInstance getLRInstance() {
        if (LRInstance == null) {
            LRInstance = new LRInstance();
        }
        return LRInstance;
    }

    private void initSVM() {
        logisticRegression = LogisticRegression.create();
        SettingsLR.setSettings(logisticRegression);
    }

    public LogisticRegression getInstance() {
        return logisticRegression;
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
            logisticRegression.train(trainData.getSamples(), Ml.ROW_SAMPLE, trainData.getResponses());
            System.out.println("LogisticRegression finish");
        } catch (VocabularyNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private TrainData trainingData() throws VocabularyNotFoundException {
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
