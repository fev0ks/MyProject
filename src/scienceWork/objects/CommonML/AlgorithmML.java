package scienceWork.objects.CommonML;

import org.opencv.core.Mat;
import org.opencv.ml.TrainData;
import scienceWork.Exceptions.VocabularyNotFoundException;

public interface AlgorithmML<T> {
    void train();

    float predict(Mat template);

    T getInstance();

    String toString();

    TrainData intiTrainData() throws VocabularyNotFoundException;
}
