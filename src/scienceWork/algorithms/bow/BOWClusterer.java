package scienceWork.algorithms.bow;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import scienceWork.Workers.FileWorker;
import scienceWork.Workers.PictureWorker;
import scienceWork.algorithms.Interfaces.Clusterer;
import scienceWork.algorithms.bow.bowTools.BOWImgDescriptorExtractor;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.picTypesData.BOWVocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mixa1 on 23.02.2018.
 */
public class BOWClusterer implements Clusterer {
    BOWImgDescriptorExtractor extractor;

    public BOWClusterer() {
        this.extractor = getBOWImgDescriptorExtractor();
    }

    private BOWImgDescriptorExtractor getBOWImgDescriptorExtractor() {
        DescriptorExtractor descriptor = DescriptorExtractor.create(Settings.getMethodDescr());
        DescriptorMatcher matcher = DescriptorMatcher.create(Settings.getMethodMatcher());
        return new BOWImgDescriptorExtractor(descriptor, matcher);
    }

    @Override
    public void findPictureType(List<Picture> pictList) {
//        new PictureClusters().findPicturesClusters(pictList, progress);
        Map<String, Mat> allTypeClusters = BOWVocabulary.vocabularies;
        for (Picture picture : pictList) {
            MatOfKeyPoint keyPoints = picture.getDescriptorProperty().getMatOfKeyPoint();
            Mat descriptors = picture.getDescriptorProperty().getMatOfDescription();
            Mat imageMat = PictureWorker.getMatFromImage(FileWorker.getInstance().loadBufferedImage(picture.getPicFile()));
            double minDistance = Double.MAX_VALUE;
            double distance;
            String bestGroup = "";
            List<List<Integer>> pointIdxsOfClusters = new ArrayList<>();
            extractor.compute(descriptors, descriptors, pointIdxsOfClusters);
//            for (int i = 0; i < clustersOfPicture.height(); i++) {
//                for (Map.Entry<String, Mat> templateCluster : allTypeClusters.entrySet()) {
//                    for (int j = 0; j < templateCluster.getValue().height(); j++) {
//                        distance = clusterTools.findDistanceForMats(clustersOfPicture, templateCluster.getValue().row(j));
//                        if (distance < minDistance) {
//                            minDistance = distance;
//                            bestGroup = templateCluster.getKey();
//                        }
//                    }
//                }
//            }

            picture.setPictureType(bestGroup);
        }
    }
}
