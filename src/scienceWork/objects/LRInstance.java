package scienceWork.objects;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.ml.LogisticRegression;
import org.opencv.ml.Ml;
import org.opencv.ml.TrainData;
import scienceWork.Exceptions.VocabularyNotFoundException;
import scienceWork.objects.CommonML.AlgorithmMLImpl;
import scienceWork.objects.constants.SettingsLR;

import java.util.Arrays;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_32FC1;

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
            samples.convertTo(samples, CV_32FC1);
            response.convertTo(response, CV_32FC1);
            logisticRegression.train(samples, Ml.ROW_SAMPLE, response);
//            logisticRegression.save("lalalal.xml");
//            logisticRegression.train(trainData);
            System.out.println("LogisticRegression finish " + logisticRegression.getLearningRate() );
        } catch (VocabularyNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public float predict(Mat template) {
        return logisticRegression.predict(template);
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

    public void showMat(Mat mat) {
        long[][] newMat = new long[mat.rows()][mat.cols()];
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                System.out.print(Math.round(mat.get(i, j)[0]) + " ");
//                newMat[i][j] = Math.round(mat.get(i, j)[0]);
            }
            System.out.println();
        }
        System.out.println(Arrays.deepToString(newMat));
    }
}
