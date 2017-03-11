package scienceWork.algorithms;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import scienceWork.Interfaces.IAlgorithm;
import scienceWork.ObjectClasses.Picture;

import java.util.Date;

public class Descriptor implements IAlgorithm {

    private Descriptor() {

    }

    private Descriptor instance;

    public Descriptor getInstance() {
        if (instance == null) {
            instance = new Descriptor();
        }
        return instance;
    }
//TODO
    private MatOfKeyPoint createKeyPoint(Picture imgBit, int methodKP) {
        final long currentDate1 = new Date(System.currentTimeMillis()).getTime();
        MatOfKeyPoint keyPoint = new MatOfKeyPoint();
        FeatureDetector detector = FeatureDetector.create(methodKP);

        detector.detect(imgBit.getMat(), keyPoint);
        final long currentDate2 = new Date(System.currentTimeMillis()).getTime();
        imgBit.setKeyP(keyPoint);
        imgBit.timeKP += currentDate2 - currentDate1;
        imgBit.countKP += keyPoint.rows();
        return keyPoint;
    }
//TODO
    //ищем Descriptor
    private Mat createDescriptor(MyBitmap imgBit, int methodD) {
        final long Date1 = new Date(System.currentTimeMillis()).getTime();
        Mat descriptor = new Mat();
        DescriptorExtractor descrip = DescriptorExtractor.create(methodD);
        descrip.compute(imgBit.getMat(), imgBit.getKeyP(), descriptor);
        imgBit.setDescr(descriptor);
        final long Date2 = new Date(System.currentTimeMillis()).getTime();
        imgBit.timeD += (Date2 - Date1);
        return descriptor;
    }
}
