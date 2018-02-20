package scienceWork.algorithms;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import scienceWork.FileWorker;
import scienceWork.objects.DescriptorProperty;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;

/* класс для получения особый точек и их дескрипторов */
public class KeyPointsAndDescriptors {

    private int numberOfMethodDescript = Settings.getMethodDescr();
    private int numberOfMethodKeyPoint = Settings.getMethodKP();

        public Picture setMatOfKPandDescr(Picture picture) {
        Mat matFromImage = PictureWorker.getMatFromImage(FileWorker.getInstance().loadBufferedImage(picture.getPicFile()));
        MatOfKeyPoint matOfKeyPoint = createKeyPoint(matFromImage, numberOfMethodKeyPoint);
        Mat matDescriptor = createDescriptor(matFromImage, matOfKeyPoint, numberOfMethodDescript);
        picture.setDescriptorProperty(new DescriptorProperty(matOfKeyPoint, matDescriptor));
        return picture;
    }



    //ищем KeyPoints
    private MatOfKeyPoint createKeyPoint(Mat matImage, int numberOfMethodKP) {
        MatOfKeyPoint keyPoint = new MatOfKeyPoint();
        FeatureDetector detector = FeatureDetector.create(numberOfMethodKP);
        detector.detect(matImage, keyPoint);
        return keyPoint;
    }

    //ищем DescriptorsOfPictureClustering
    private Mat createDescriptor(Mat matImage, MatOfKeyPoint keyPointOfImage, int methodD) {
        Mat descriptor = new Mat();
        DescriptorExtractor descrip = DescriptorExtractor.create(methodD);
        descrip.compute(matImage, keyPointOfImage, descriptor);
        return descriptor;
    }


}
