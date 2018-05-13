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
        annMlp.setActivationFunction(ANN_MLP.SIGMOID_SYM);

        Mat layers = new Mat(1,10,CvType.CV_32F);
        layers.put(0,0, Settings.getCountBOWClusters());
        layers.put(0,1, Settings.getCountBOWClusters()*2);
        layers.put(0,2, Settings.getCountBOWClusters()*2);
        layers.put(0,3, Settings.getCountBOWClusters()*2);
        layers.put(0,4, Settings.getCountBOWClusters());
        layers.put(0,5, Settings.getCountBOWClusters()/2);
        layers.put(0,6, Settings.getCountBOWClusters()/4);
        layers.put(0,7, Settings.getCountBOWClusters()/6);
        layers.put(0,8, Settings.getCountBOWClusters()/8);
        layers.put(0,9,  BOWVocabulary.vocabularies.size());

        annMlp.setLayerSizes(layers);
        annMlp.setTrainMethod(ANN_MLP.BACKPROP);
        annMlp.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 10000, 0.0000001));
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
