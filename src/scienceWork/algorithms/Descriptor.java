package scienceWork.algorithms;

import org.opencv.core.CvType;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import scienceWork.ObjectClasses.DescriptorProperty;
import scienceWork.ObjectClasses.Picture;
import scienceWork.WorkFolder;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Descriptor implements Runnable {
    private Picture picture;

    public Descriptor(Picture picture) {
        this.picture = picture;
    }

    @Override
    public void run() {
        double value=getNorm();
        picture.getValueAlgorithm().setNormKeyPoints(getNorm());
        System.out.println(picture.getName()+"   values="+value+"\n");
    }

    private double getValue() {
        // DescriptorProperty descriptorProperty = getDescriptor(getMatFromImage(picture.getImage()), 12, 7);//получаем КP/D AKAZE
        //получаем Д
        //фильтруем похожие Д -> меньше КР
        //вычисляем норму:
        //вычисляем центр масс расположения КР
        //вычисляем среднее расстояне от КР до него

        //нужно знать среднее разрешение всех фото
        //приводим норму в соответствие к среднему
        return getNorm();
    }

    private Mat getMatFromImage(BufferedImage image) {
        Mat imageMat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        imageMat.put(0, 0, data);
        return imageMat;
    }

    private DescriptorProperty getDescriptor(Mat matImage, int numberOfMethodKP, int numberOfMethodD) {
        MatOfKeyPoint matOfKeyPoint = createKeyPoint(matImage, numberOfMethodKP);
        Mat matDescriptor = createDescriptor(matImage, matOfKeyPoint, numberOfMethodD);
        return new DescriptorProperty(matOfKeyPoint, matDescriptor);
    }

    private MatOfKeyPoint createKeyPoint(Mat matImage, int numberOfMethodKP) {
        MatOfKeyPoint keyPoint = new MatOfKeyPoint();
        FeatureDetector detector = FeatureDetector.create(numberOfMethodKP);

        detector.detect(matImage, keyPoint);
        return keyPoint;
    }

    //ищем Descriptor
    private Mat createDescriptor(Mat matImage, MatOfKeyPoint keyPointOfImage, int methodD) {
        Mat descriptor = new Mat();
        DescriptorExtractor descrip = DescriptorExtractor.create(methodD);
        descrip.compute(matImage, keyPointOfImage, descriptor);
        return descriptor;
    }

    private double getNorm() {
        //TODO нежно загрузиьть изображение, его же сейчас там нет
        BufferedImage image=  WorkFolder.getInstance().loadPicFromFile(picture.getPicFile());
        MatOfKeyPoint matOfKeyPoint = createKeyPoint(getMatFromImage(image),12);
        KeyPoint[] keyPoints = matOfKeyPoint.toArray();
        System.out.println(picture.getName()+"   keyPoints length="+keyPoints.length);
        int avrgX = 0;
        int avrgY = 0;
        for (KeyPoint keyPoint : keyPoints) {
            avrgX += keyPoint.pt.x;
            avrgY += keyPoint.pt.y;
        }
        avrgX /= keyPoints.length;
        avrgY /= keyPoints.length;
        double distanceToCenterKPAvrg = 0;
        for (KeyPoint keyPoint : keyPoints) {
            distanceToCenterKPAvrg = Math.sqrt(Math.pow(avrgX - keyPoint.pt.x, 2) + Math.pow(avrgY - keyPoint.pt.y, 2));
        }
        distanceToCenterKPAvrg /= keyPoints.length;
        return distanceToCenterKPAvrg;
    }


}
