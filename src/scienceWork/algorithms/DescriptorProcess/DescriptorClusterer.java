package scienceWork.algorithms.DescriptorProcess;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.objects.Clusters;
import scienceWork.objects.Picture;

import java.util.List;
import java.util.Map;

public class DescriptorClusterer {
    private Progress progress;
    private ClusterTools clusterTools;

    public DescriptorClusterer(Progress progress) {
        this.progress = progress;
        clusterTools = new ClusterTools();
    }

    //По известным кластерам определяю типы ихображений
    public void findPictureType(List<Picture> pictList) {
        new PictureClusters().findPicturesClusters(pictList, progress);
        Map<String, Mat> allTypeClusters = Clusters.addGeneralizedClustersForInputTypeImage;
        for (Picture picture : pictList) {
            Mat clustersOfPicture = picture.getDescriptorProperty().getCentersOfDescriptors();
            double minDistance = Double.MAX_VALUE;
            double distance;
            String bestGroup = "";
            for (int i = 0; i < clustersOfPicture.height(); i++) {
                for (Map.Entry<String, Mat> templateCluster : allTypeClusters.entrySet()) {
                    for (int j = 0; j < templateCluster.getValue().height(); j++) {
                        distance = clusterTools.findDistanceForMats(clustersOfPicture, templateCluster.getValue().row(j));
                        if (distance < minDistance) {
                            minDistance = distance;
                            bestGroup = templateCluster.getKey();
                        }
                    }
                }
            }
            picture.setPictureType(bestGroup);
        }
    }
}
