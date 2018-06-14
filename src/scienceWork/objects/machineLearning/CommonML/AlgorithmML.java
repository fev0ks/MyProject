package scienceWork.objects.machineLearning.CommonML;

import org.opencv.core.Mat;
import org.opencv.ml.StatModel;
import org.opencv.ml.TrainData;
import scienceWork.Exceptions.VocabularyNotFoundException;

public interface AlgorithmML<T> {
    int getClassifierId();
    String getType();
    void train();
    boolean isTrained();

    float predict(Mat template);

    T getInstance();

    Mat getSupportVectors();

    int getFeatureID();
    int getCountClusters();
    void setInstance(T obj);

    void setFeatureID(int featureID);
    void setCountClusters(int countClusters);
    String getPath();
    void setPath(String path);

    String toString();

    TrainData intiTrainData() throws VocabularyNotFoundException;
}
