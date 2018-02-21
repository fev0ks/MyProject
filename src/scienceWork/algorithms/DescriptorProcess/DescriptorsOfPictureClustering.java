package scienceWork.algorithms.DescriptorProcess;

import org.opencv.core.Mat;
import scienceWork.algorithms.Interfaces.IProcess;
import scienceWork.objects.Picture;

/* класс для получения особый точек и их дескрипторов */
public class DescriptorsOfPictureClustering implements IProcess {
    private Picture picture;

    public DescriptorsOfPictureClustering(Picture picture) {
        this.picture = picture;
    }

    @Override
    public void run() {
       picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture));
        if (!picture.getDescriptorProperty().getMatOfDescription().empty()) {
            Mat centersOfDescriptors = new ClusterTools().createClusters(picture.getDescriptorProperty().getMatOfDescription());
            picture.getDescriptorProperty().setCentersOfDescriptors(centersOfDescriptors);
        }
    }
}
