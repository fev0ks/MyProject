package scienceWork.algorithms.bow.SVMHelper;

import org.opencv.ml.SVM;

/**
 * Created by mixa1 on 17.03.2018.
 */
public class SVMHelper {

    private SVM svm;

    public void trainSVM(){
        svm = SVM.create();
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//
//        Mat classes = new Mat();
//        Mat trainingData = new Mat();
//        Mat trainingImages = new Mat();
//        Mat trainingLabels = new Mat();
//        SVM clasificador;
//        String path="C:\\java workspace\\ora\\images\\Color_Happy_jpg";
//        for (File file : new File(path).listFiles()) {
//            Mat img=new Mat();
//            Mat con = Highgui.imread(path+"\\"+file.getName(),Highgui.CV_LOAD_IMAGE_GRAYSCALE);
//            con.convertTo(img, CvType.CV_32FC1,1.0/255.0);
//
//            img.reshape(1, 1);
//            trainingImages.push_back(img);
//            trainingLabels.push_back(Mat.ones(new Size(1, 75), CvType.CV_32FC1));
//
//        }
//        System.out.println("divide");
//        path="C:\\java workspace\\ora\\images\\Color_Sad_jpg";
//        for (File file : new File(path).listFiles()) {
//            Mat img=new Mat();
//            Mat m=new Mat(new Size(640,480),CvType.CV_32FC1);
//            Mat con = Highgui.imread(file.getAbsolutePath(),Highgui.CV_LOAD_IMAGE_GRAYSCALE);
//
//            con.convertTo(img, CvType.CV_32FC1,1.0/255.0);
//            img.reshape(1, 1);
//            trainingImages.push_back(img);
//
//            trainingLabels.push_back(Mat.zeros(new Size(1, 75), CvType.CV_32FC1));
//
//        }
//
//        trainingLabels.copyTo(classes);
//        SVMParams params = new CvSVMParams();
//        params.set_kernel_type(CvSVM.LINEAR);
//        CvType.typeToString(trainingImages.type());
//        SVM svm=new CvSVM();
//
//
//
//        clasificador = new CvSVM(trainingImages,classes, new Mat(), new Mat(), params);
//
//        clasificador.save("C:\\java workspace\\ora\\images\\svm.xml");
//        Mat out=new Mat();
//
//        clasificador.load("C:\\java workspace\\ora\\images\\svm.xml");
//        Mat sample=Highgui.imread("C:\\java workspace\\ora\\images\\Color_Sad_jpg\\EMBfemale20-2happy.jpg",Highgui.CV_LOAD_IMAGE_GRAYSCALE);
//
//        sample.convertTo(out, CvType.CV_32FC1,1.0/255.0);
//        out.reshape(1, 75);
//        System.out.println(clasificador.predict(out));
    }

}
