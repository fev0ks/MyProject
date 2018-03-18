package scienceWork.algorithms.DescriptorProcess;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.Interfaces.Teacher;
import scienceWork.objects.picTypesData.ImgTypesClusters;
import scienceWork.objects.Picture;

import java.util.List;


public class DescriptorTeacher implements Teacher {
    private List<Picture> pictList;
    private Progress progress;
    private ClusterTools clusterTools;

    public DescriptorTeacher(List<Picture> pictList, Progress progress) {
        this.pictList = pictList;
        this.progress = progress;
        clusterTools = new ClusterTools();
    }

    //Вычисляю для заданного типа изображений их особенности
    public void findFeatures() {
        //ищу кластеры для каждой фотки
        new PictureClusters().findPicturesClusters(pictList, progress);
        //собираю все их вместе и ищу обшие кластеры
        Mat commonClusters = clusterTools.findCommonPicturesCluster(pictList);
        String typeImages = pictList.get(0).getPictureType();
        //Добавляю эти кластеры для указанного входного типа
        if (commonClusters.height() != 0)
            ImgTypesClusters.trainedClusters.put(typeImages, commonClusters);
//        Mat bestCommonClusters = filteringClusters(commonClusters);
//        printMat(commonClusters);
    }




}
