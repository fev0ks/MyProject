package scienceWork.algorithms.DescriptorProcess;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.objects.Picture;
import scienceWork.objects.picTypesData.ImgTypesClusters;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class DescriptorClusterer {
    private Progress progress;
    private ClusterTools clusterTools;

    public DescriptorClusterer(Progress progress) {
        this.progress = progress;
        clusterTools = new ClusterTools();
    }

    //По известным кластерам определяю типы ихображений
    public void findPictureType(List<Picture> pictList) {

        int k = 5;

        new PictureClusters().findPicturesClusters(pictList, progress);
        Map<String, Mat> allTypeClusters = ImgTypesClusters.addGeneralizedClustersForInputTypeImage;
        for (Picture picture : pictList) {
            System.out.println(picture.getName() + " " + picture.getPictureType());
            Mat clustersOfPicture = picture.getDescriptorProperty().getCentersOfDescriptors();
            double minDistance = Double.MAX_VALUE;
            double distance = 0;
            double totalDistance = 0;
            String bestGroup = "";

//            List<Double> distances = new ArrayList<>();


            for (Map.Entry<String, Mat> templateCluster : allTypeClusters.entrySet()) {
                SortedSet<Double> kMeans = new TreeSet<>();
                for (int i = 0; i < clustersOfPicture.height(); i++) {
                    totalDistance = 0;
                    for (int j = 0; j < templateCluster.getValue().height(); j++) {
                        distance = clusterTools.findDistanceForMats(clustersOfPicture, templateCluster.getValue().row(j));
                        kMeans.add(distance);
                    }
//                    System.out.println(i+" kMeans ");
//                    kMeans.stream().limit(5).forEach(s -> System.out.print(s + " "));


                }
                totalDistance = kMeans.stream().mapToDouble(d -> d).limit(4).sum();
                System.out.println("Group: " + templateCluster.getKey() + " " + totalDistance + " <-> " + minDistance);
                if (totalDistance < minDistance) {
                    minDistance = totalDistance;
                    bestGroup = templateCluster.getKey();
                }
            }
            System.out.println("Best Group: " + bestGroup + " " + minDistance);
            picture.setExitPictureType(bestGroup);
        }
    }
}
