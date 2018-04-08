package scienceWork.objects.CommonML;

import org.opencv.ml.TrainData;
import scienceWork.Exceptions.VocabularyNotFoundException;

public interface AlgorithmML<T> {
    void train();

    T getInstance();

    String toString();

    TrainData intiTrainData() throws VocabularyNotFoundException;
}
