package scienceWork.algorithms;

import org.opencv.core.Mat;
import scienceWork.algorithms.DescriptorProcess.ClusterTools;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
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
        try {
            picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture));
        }catch (Exception e){
            System.out.println(picture.toString());
        }
        if (!picture.getDescriptorProperty().getMatOfDescription().empty()) {
            Mat centersOfDescriptors = new ClusterTools().createClusters(picture.getDescriptorProperty().getMatOfDescription());
            picture.getDescriptorProperty().setCentersOfDescriptors(centersOfDescriptors);
        }
    }
}
