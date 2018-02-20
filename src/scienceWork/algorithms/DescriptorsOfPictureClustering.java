package scienceWork.algorithms;

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
        System.out.println(picture.getName());
        picture = new KeyPointsAndDescriptors().setMatOfKPandDescr(picture);
        if(picture.getDescriptorProperty().getMatOfDescription().height()>0) {
            Mat centersOfDescriptors = new ClusterTools().createClusters(picture.getDescriptorProperty().getMatOfDescription());
            picture.getDescriptorProperty().setCentersOfDescriptors(centersOfDescriptors);
        }
    }

    private Mat getIfExistDescriptorsInDB(){

        return null;
    }
}
