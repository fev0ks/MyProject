package scienceWork.objects.machineLearning.mlSettings;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.ANN_MLP;
import org.opencv.ml.SVM;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.data.BOWVocabulary;

public class SettingsANNMLP {

    public static void setSettings(ANN_MLP annMlp) {

        Mat layers = new Mat(1,4, CvType.CV_32S);
        layers.put(0,0, Settings.getCountBOWClusters());
        layers.put(0,1, BOWVocabulary.vocabularies.size()*2);
        layers.put(0,2, BOWVocabulary.vocabularies.size()*2);
        layers.put(0,3,  BOWVocabulary.vocabularies.size());
//        layers.put(0,4,  BOWVocabulary.vocabularies.size());
        annMlp.setLayerSizes(layers);

        annMlp.setActivationFunction(ANN_MLP.GAUSSIAN, 1, 1);
        annMlp.setTrainMethod(ANN_MLP.BACKPROP, 0.01, 0.01);


        annMlp.setTermCriteria(new TermCriteria(TermCriteria.EPS, 100000, 0.00001));
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
