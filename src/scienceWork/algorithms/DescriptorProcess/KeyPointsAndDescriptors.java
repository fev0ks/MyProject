package scienceWork.algorithms.DescriptorProcess;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import scienceWork.Workers.FileWorker;
import scienceWork.Workers.PictureWorker;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.pictureData.DescriptorProperty;

/* класс для получения особый точек и их дескрипторов */
public class KeyPointsAndDescriptors {

    private int numberOfMethodDescript = Settings.getMethodDescr();
    private int numberOfMethodKeyPoint = Settings.getMethodKP();

        public DescriptorProperty calculateDescriptorProperty(Picture picture) throws Exception{
        Mat matFromImage = PictureWorker.getMatFromImage(FileWorker.getInstance().loadBufferedImage(picture.getPicFile()));
        MatOfKeyPoint matOfKeyPoint = createKeyPoint(matFromImage, numberOfMethodKeyPoint);
        Mat matDescriptor = createDescriptor(matFromImage, matOfKeyPoint, numberOfMethodDescript);
        
        return new DescriptorProperty(matOfKeyPoint, matDescriptor);
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
        descriptor.convertTo(descriptor, CvType.CV_32F);
        return descriptor;
    }
//    //найти особые точки их дескрипторы, по ним найти центры кластеров дескрипторов изображения
//    public void findPicturesClusters(List<Picture> pictList, Progress progress) {
////        ExecutorService executor = Executors.newFixedThreadPool(Settings.getCountThreads());
//        ExecutorService executor = Executors.newFixedThreadPool(Settings.getInstance().getCountThreads());
//
//        List<Future> futureList = new LinkedList<>();
//        System.out.println("start find KP/Descr");
//        int number = 0;
//        for (Picture picture : pictList) {
//            DescriptorsOfPictureClustering descriptorsOfPictureClustering = new DescriptorsOfPictureClustering(picture);
//            try {
//                futureList.add(executor.submit(descriptorsOfPictureClustering));
//                descriptorsOfPictureClustering.run();
//                progress.setProgress( number++ , pictList.size());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (int i = 0; i < pictList.size(); i++) {
////            System.out.println("progress " + i);
//
//
//            while (!futureList.get(i).isDone()) {
//
//            }
//        }
//        executor.shutdown();
//        System.out.println("finish find KP/Descr");
//    }

}
