package scienceWork.objects.constants;

import org.opencv.core.TermCriteria;
import org.opencv.ml.SVM;

public class SettingsSVM {

    public static void setSettings(SVM svm){
        svm.setKernel(SVM.INTER);
        svm.setDegree(1);
        svm.setGamma(0.1);
        svm.setType(SVM.NU_SVC);
        svm.setCoef0(0.00001);
        svm.setC(10);
        svm.setNu(0.1);
        svm.setP(0.000001);
        svm.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 10000, 0.1));
    }
}
