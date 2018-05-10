package scienceWork.objects.machineLearning.mlSettings;

import org.opencv.core.TermCriteria;
import org.opencv.ml.ANN_MLP;
import org.opencv.ml.SVM;

public class SettingsANNMLP {

    public static void setSettings(ANN_MLP annMlp) {
        annMlp.setActivationFunction(ANN_MLP.SIGMOID_SYM);
//        annMlp.setTrainMethod(ANN_MLP.ANNEAL);
        annMlp.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 10000, 0.1));
    }

//    enum  	ActivationFunctions {
//        IDENTITY = 0,
//        SIGMOID_SYM = 1,
//        GAUSSIAN = 2
//    }
//    possible activation functions More...
//
//    enum  	Flags {
//        UPDATE_MODEL = 1,
//        RAW_OUTPUT =1,
//        COMPRESSED_INPUT =2,
//        PREPROCESSED_INPUT =4
//    }
//    Predict options. More...
//
//    enum  	TrainFlags {
//        UPDATE_WEIGHTS = 1,
//        NO_INPUT_SCALE = 2,
//        NO_OUTPUT_SCALE = 4
//    }
//    Train options. More...
//
//    enum  	TrainingMethods {
//        BACKPROP =0,
//        RPROP =1
//    }
//    Available training methods. More...
}
