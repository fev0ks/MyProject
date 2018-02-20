package scienceWork.algorithms;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import scienceWork.objects.constants.Settings;

import static org.opencv.core.CvType.CV_32FC3;

public class ClusterTools {

    public Mat createClusters(Mat matDescriptor) {
        return clusteringDescriptors(matDescriptor);
    }

    private Mat clusteringDescriptors(Mat matDescriptor) {
        Mat clusteredCenter = new Mat();
        if (matDescriptor != null && matDescriptor.height() > 0) {
            Mat clusteredHSV = new Mat();

            matDescriptor.convertTo(matDescriptor, CV_32FC3);
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
            if (matDescriptor.height() < Settings.getCountClusters()) {
                setFictitiousDescriptors(matDescriptor);
            }

            double kMeans = Core.kmeans(matDescriptor, Settings.getCountClusters(), clusteredHSV, criteria, 100, Core.KMEANS_RANDOM_CENTERS, clusteredCenter);
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
        System.out.println("setFictitiousDescriptors " + Settings.getCountClusters() / matDescriptor.height());
        int countOfNeedRows;
        while ((countOfNeedRows = Settings.getCountClusters() / matDescriptor.height()) != 0) {
            matDescriptor.push_back(matDescriptor);
            System.out.println(countOfNeedRows + " " + Settings.getCountClusters() / matDescriptor.height() + " matDescriptor " + matDescriptor.height() + "x" + matDescriptor.width());
        }
        return matDescriptor;
    }

    public double findDistanceForMats(Mat mat1, Mat mat2) {
        double distance = 0;
        if (mat1.width() != mat2.width()) return Double.MAX_VALUE;
        for (int i = 0; i < mat1.width(); i++) {
            distance += Math.abs(mat1.get(0, i)[0] - mat2.get(0, i)[0]);
        }
        return distance;
    }
}
