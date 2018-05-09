package scienceWork.objects.CommonML;

import org.opencv.core.Mat;
import org.opencv.ml.TrainData;
import scienceWork.Exceptions.VocabularyNotFoundException;

public interface AlgorithmML<T> {
    int getClassifierId();

    void train();

    float predict(Mat template);

    T getInstance();

    Mat getSupportVectors();

    int getFeatureID();
    int getCountClusters();
    void setInstance(T obj);

    void setFeatureID(int featureID);
    void setCountClusters(int countClusters);

    String toString();

    TrainData intiTrainData() throws VocabularyNotFoundException;
}
