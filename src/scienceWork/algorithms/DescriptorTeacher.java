package scienceWork.algorithms;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.Interfaces.Teacher;
import scienceWork.objects.Clusters;
import scienceWork.objects.Picture;

import java.util.List;


public class DescriptorTeacher implements Teacher {
    private List<Picture> pictList;
    private Progress progress;
    private ClusterTools clusterTools;

    public DescriptorTeacher() {

    }

    public DescriptorTeacher(List<Picture> pictList, Progress progress) {
        this.pictList = pictList;
        this.progress = progress;
        clusterTools = new ClusterTools();
    }

    @Override
    public void run() {
        new Thread(this::findFeatures).start();
    }

    //Вычисляю для заданного типа изображений их особенности
    private void findFeatures() {
        //ищу кластеры для каждой фотки
        clusterTools.findPicturesClusters(pictList, progress);
        //собираю все их вместе и ищу обшие кластеры
        Mat commonClusters = findCommonPicturesCluster(pictList);
        String typeImages = pictList.get(0).getPictureType();
        //Добавляю эти кластеры для указанного входного типа
        if (commonClusters.height() != 0)
            Clusters.addGeneralizedClustersForInputTypeImage.put(typeImages, commonClusters);
//        Mat bestCommonClusters = filteringClusters(commonClusters);
//        printMat(commonClusters);
    }



    //Собрал в кучу все кластеры и нашел N самых самых* но это не точно
    private Mat findCommonPicturesCluster(List<Picture> pictList) {
        Mat allClusters = new Mat();
        for (Picture picture : pictList) {
            try {
                if (picture.getDescriptorProperty().getCountOfDescr() > 0) {
                    allClusters.push_back(picture.getDescriptorProperty().getCentersOfDescriptors());
                }
            } catch (NullPointerException e) {
//                System.out.println(e);
                System.out.println(picture.getName() + " NullPointerException;" +
                        " MatOfDescription: " + picture.getDescriptorProperty().getMatOfDescription());
            }
        }
        Mat commonClusters = clusterTools.createClusters(allClusters);

        return commonClusters;
    }


}
