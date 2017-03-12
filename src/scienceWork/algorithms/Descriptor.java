package scienceWork.algorithms;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import scienceWork.Interfaces.IAlgorithm;
import scienceWork.ObjectClasses.DescriptorProperty;

import java.util.Date;

public class Descriptor implements IAlgorithm {

    private Descriptor() {

    }
    public double getValue(){
        //олучаем КР
        //получаем Д
        //фильтруем похожие Д -> меньше КР
        //вычисляем норму
            //вычисляем центр масс расположения КР
            //вычисляем среднее расстояне от КР до него

        //нужно знать среднее разрешение всех фото
            //приводим норму в соответствие к среднему
        return 1/1;
    }
    private Descriptor instance;

    public Descriptor getInstance() {
        if (instance == null) {
            instance = new Descriptor();
        }
        return instance;
    }

    private DescriptorProperty getDescriptor(Mat matImage, int numberOfMethodKP, int numberOfMethodD) {
        MatOfKeyPoint matOfKeyPoint = createKeyPoint(matImage, numberOfMethodKP);
        Mat matDescriptor = createDescriptor(matImage, matOfKeyPoint, numberOfMethodD);
        DescriptorProperty featureDetector = new DescriptorProperty(matOfKeyPoint, matDescriptor);

        return featureDetector;
    }

    private MatOfKeyPoint createKeyPoint(Mat matImage, int numberOfMethodKP) {
        final long currentDate1 = new Date(System.currentTimeMillis()).getTime();
        MatOfKeyPoint keyPoint = new MatOfKeyPoint();
        FeatureDetector detector = FeatureDetector.create(numberOfMethodKP);

        detector.detect(matImage, keyPoint);
        final long currentDate2 = new Date(System.currentTimeMillis()).getTime();
        return keyPoint;
    }

    //ищем Descriptor
    private Mat createDescriptor(Mat matImage, MatOfKeyPoint keyPointOfImage, int methodD) {
        final long Date1 = new Date(System.currentTimeMillis()).getTime();
        Mat descriptor = new Mat();
        DescriptorExtractor descrip = DescriptorExtractor.create(methodD);
        descrip.compute(matImage, keyPointOfImage, descriptor);
        return descriptor;
    }
}
//TODO
//Фильтр
//норма
//

//    ArrayList<MatOfDMatch> matches
//
//    List<DMatch> good_matches = new ArrayList<DMatch>();
//    double maxDist=0,minDist=0;
//        try {
//                Iterator<MatOfDMatch> iterator = matches.iterator();
//        DMatch[] matOfDMatch;
//
//        for (; iterator.hasNext(); ) {
//        matOfDMatch = iterator.next().toArray();
//        if(max<matOfDMatch[0].distance) max=matOfDMatch[0].distance;
//        if(min>matOfDMatch[0].distance) min=matOfDMatch[0].distance;
//
//        if (matOfDMatch[0].distance<maxDist & matOfDMatch[0].distance / matOfDMatch[1].distance < distanceValue) {
//        good_matches.add(matOfDMatch[0]);
//        }