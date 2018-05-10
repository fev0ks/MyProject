package scienceWork.objects.machineLearning.mlSettings;

import org.opencv.core.TermCriteria;
import org.opencv.ml.SVMSGD;

public class SettingsSVMSGD {

    public static void setSettings(SVMSGD svmsgd){

//        setSvmsgdType(), setMarginType(), setMarginRegularization(), setInitialStepSize(), and setStepDecreasingPower().
//        SGD Stochastic Gradient Descent.
//        ASGD Average Stochastic Gradient Descent.
        svmsgd.setSvmsgdType(SVMSGD.SGD);
        svmsgd.setMarginType(SVMSGD.SOFT_MARGIN);
        svmsgd.setStepDecreasingPower(0.9F);
        svmsgd.setMarginRegularization(0.7F);
        svmsgd.setInitialStepSize(0.6F);

        svmsgd.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 10000, 0.1));
    }
}
