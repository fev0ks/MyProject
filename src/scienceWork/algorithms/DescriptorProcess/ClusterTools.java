package scienceWork.algorithms.DescriptorProcess;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;

import java.util.List;

import static org.opencv.core.CvType.CV_32FC3;

@Deprecated
public class ClusterTools {

    public Mat createClusters(Mat matDescriptor) {
        return clusteringDescriptors(matDescriptor);
    }

    private Mat clusteringDescriptors(Mat matDescriptor) {
        Mat clusteredCenter = new Mat();
        if (matDescriptor != null && matDescriptor.height() > 0) {
            Mat clusteredHSV = new Mat();

            try {
                matDescriptor.convertTo(matDescriptor, CV_32FC3);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
//        mHSV.convertTo(mHSV, CvType.CV_32FC3);
//        TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 100, 0.1);
            TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 1000, 0.01);

        /*
        samples — Floating-point матрица входной выборки (по одной строке на каждый образец);
        clusterCount – количество кластеров;
        labels – ссылка на входной/выходной массив, который будет хранить индексы кластера для каждого образца;
        termcrit – задает максимальное число итераций и/или точность (расстояние центров может двигаться в пределах
            между последующими итерациями);
        attempts — сколько раз этот алгоритм выполняется с использованием различных начальных меток.
            Алгоритм возвращает labels, которые дают лучшие характеристик компактности (в соответствии с последним параметром функции);
        flags:
        KMEANS_RANDOM_CENTERS – в каждой попытке выбираются случайные начальные центры;
        KMEANS_PP_CENTERS – используется kmeans++ инициализация центров;
        KMEANS_USE_INITIAL_LABELS – во время первой (и возможно единственной попытке) функция использует предоставляемые пользователем labels.
            Для второй и последующих попыток используются случайные или полу случайные центры;
        centers – выходная матрица центров, по одной строке на каждый кластер.
         */

//            System.out.println("matDescriptor " + matDescriptor.height() + "x" + matDescriptor.width());
            if (matDescriptor.height() < Settings.getCountWords()) {
                setFictitiousDescriptors(matDescriptor);
            }
            try {
                Core.kmeans(matDescriptor, Settings.getCountWords(), clusteredHSV, criteria, 100, Core.KMEANS_PP_CENTERS, clusteredCenter);
            } catch (Exception e) {
                System.out.println("ClusterTools.clusteringDescriptors -  Core.kmeans");
                System.out.println("matDescriptor " + matDescriptor.height() + "x" + matDescriptor.width());
            }
//            System.out.println(" kMean: " + kMeans);
//            System.out.println("clusteredCenter: " + clusteredCenter);
//        System.out.println("clusteredCenter: " + picture.getName() + " [");
//        for (int i = 0; i < clusteredCenter.height(); i++) {
//            for (int j = 0; j < clusteredCenter.width(); j++) {
//                System.out.print(" " + Arrays.toString(clusteredCenter.get(i, j)));
//            }
//            System.out.println("\n");
//        }
//        System.out.print(" ]\n");
        }
        return clusteredCenter;
    }

    private Mat setFictitiousDescriptors(Mat matDescriptor) {
//        System.out.println("setFictitiousDescriptors " + Settings.getCountWords() / matDescriptor.height());
        int countOfNeedRows;
        while ((countOfNeedRows = Settings.getCountWords() / matDescriptor.height()) != 0) {
            matDescriptor.push_back(matDescriptor);
//            System.out.println(countOfNeedRows + " " + Settings.getCountWords() / matDescriptor.height() + " matDescriptor " + matDescriptor.height() + "x" + matDescriptor.width());
        }
        return matDescriptor;
    }

    public double findDistanceForMats(Mat mat1, Mat mat2) {
        double distance = 0;
        if (mat1.width() != mat2.width()) return Double.MAX_VALUE;
        for (int i = 0; i < mat1.width(); i++) {
//            for(int j=0; j<mat2.width();j++)

            distance += Math.abs((int)mat1.get(0, i)[0] ^ (int)mat2.get(0, i)[0]);
        }
        return distance;
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

    //Собрал в кучу все кластеры и нашел N самых самых* но это не точно
    public Mat findCommonPicturesCluster(List<Picture> pictList) {
        Mat allClusters = new Mat();
        for (Picture picture : pictList) {
            try {
                if (picture.getDescriptorProperty().getCountOfDescr() > 0) {
                    Mat centersOfDescriptors = picture.getDescriptorProperty().getCentersOfDescriptors();
                    if (centersOfDescriptors.height() != 0)
                        allClusters.push_back(centersOfDescriptors);
                }
            } catch (NullPointerException e) {
//                System.out.println(e);
                System.out.println(picture.getName() + " NullPointerException;" +
                        " MatOfDescription: " + picture.getDescriptorProperty().getMatOfDescription());
            }
        }

        return createClusters(allClusters);
    }
}
