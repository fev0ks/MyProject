package scienceWork.algorithms.DescriptorProcess;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import scienceWork.Workers.FileWorker;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.pictureData.DescriptorProperty;

/* класс для получения особый точек и их дескрипторов */
public class KeyPointsAndDescriptors {

    private int numberOfMethodDescript = Settings.getMethodDescr();
    private int numberOfMethodKeyPoint = Settings.getMethodKP();

        public DescriptorProperty calculateDescriptorProperty(Picture picture){
//        Mat matFromImage = PictureWorker.getMatFromImage(FileWorker.getInstance().loadBufferedImage(picture.getPicFile()));
        Mat matFromImage = FileWorker.getInstance().getMatFromFileOfImage(picture.getPicFile(), true, true);
        MatOfKeyPoint matOfKeyPoint = createKeyPoint(matFromImage, numberOfMethodKeyPoint);
        Mat matDescriptor = createDescriptor(matFromImage, matOfKeyPoint, numberOfMethodDescript);
        
        return new DescriptorProperty(matOfKeyPoint, matDescriptor);
    }

    //ищем KeyPoints
    private MatOfKeyPoint createKeyPoint(Mat matImage, int numberOfMethodKP) {
        MatOfKeyPoint keyPoint = new MatOfKeyPoint();
        FeatureDetector detector = FeatureDetector.create(numberOfMethodKP);
//        FastFeatureDetector detector = FastFeatureDetector.create(25, true,0);
//        FastFeatureDetector detector = FastFeatureDetector.create();
        detector.detect(matImage, keyPoint);
        return keyPoint;
    }

    //ищем DescriptorsOfPictureClustering
    private Mat createDescriptor(Mat matImage, MatOfKeyPoint keyPointOfImage, int methodD) {
        Mat descriptor = new Mat();
        DescriptorExtractor descrip = DescriptorExtractor.create(methodD);
        descrip.compute(matImage, keyPointOfImage, descriptor);
        descriptor.convertTo(descriptor, CvType.CV_32F);
        return descriptor;
    }
}
