package scienceWork.objects.machineLearning.mlSettings;

import org.opencv.core.TermCriteria;
import org.opencv.ml.SVM;

public class SettingsSVM {

    public static void setSettings(SVM svm){
//        svm.setKernel(SVM.INTER);

//        svm.setDegree(1);
//        svm.setGamma(0.1);
        svm.setType(SVM.NU_SVC);
//        svm.setCoef0(0.00001);
//        svm.setC(0.01);
        svm.setNu(0.105);
//        svm.setP(0.000001);
        svm.setKernel(SVM.INTER);
        svm.setTermCriteria(new TermCriteria(TermCriteria.EPS, 100000, 0.01));
    }
    /*
    degree – Parameter degree of a kernel function (POLY).
    gamma – Parameter \gamma of a kernel function (POLY / RBF / SIGMOID / CHI2).
    coef0 – Parameter coef0 of a kernel function (POLY / SIGMOID).
    Cvalue – Parameter C of a SVM optimization problem (C_SVC / EPS_SVR / NU_SVR).
    nu – Parameter \nu of a SVM optimization problem (NU_SVC / ONE_CLASS / NU_SVR).
    p – Parameter \epsilon of a SVM optimization problem (EPS_SVR).
     */
}
