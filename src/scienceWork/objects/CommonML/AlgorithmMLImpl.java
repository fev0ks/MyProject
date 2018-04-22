package scienceWork.objects.CommonML;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.ml.Ml;
import org.opencv.ml.TrainData;
import scienceWork.Exceptions.VocabularyNotFoundException;
import scienceWork.objects.picTypesData.BOWVocabulary;

import java.util.Map;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_32S;

public abstract class AlgorithmMLImpl<T> implements AlgorithmML<T> {
    public abstract void train();

    public abstract T getInstance();

    public abstract String toString();

    public TrainData intiTrainData() throws VocabularyNotFoundException {
        Mat trainingData = new Mat();
        byte classNumber = 0;
        Mat classes = new Mat();
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

//            trainingData.convertTo(trainingData, CV_32F);
            classes.convertTo(classes, CV_32S);
            return TrainData.create(trainingData, Ml.ROW_SAMPLE, classes);
        } else {
            throw new VocabularyNotFoundException("BOWVocabulary.vocabularies Empty");
        }
    }
}
