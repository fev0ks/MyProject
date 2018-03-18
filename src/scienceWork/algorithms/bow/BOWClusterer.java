package scienceWork.algorithms.bow;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.Workers.FileWorker;
import scienceWork.Workers.PictureWorker;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
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
    private Progress progress;

    public BOWClusterer(Progress progress) {
        this.progress = progress;
        this.extractor = getBOWImgDescriptorExtractor();
    }

    private BOWImgDescriptorExtractor getBOWImgDescriptorExtractor() {
        DescriptorExtractor descriptor = DescriptorExtractor.create(Settings.getMethodDescr());
        DescriptorMatcher matcher = DescriptorMatcher.create(Settings.getMethodMatcher());
        return new BOWImgDescriptorExtractor(descriptor, matcher);
    }

    @Override
    public void findPictureType(List<Picture> pictureList) {
//        new PictureClusters().findPicturesClusters(pictList, progress);
        Map<String, Mat> allTypeVocabularies = BOWVocabulary.vocabularies;
        long countPictures = pictureList.size();
        long count =0;
        for (Picture picture : pictureList) {
            progress.setProgress(count, countPictures);;
            MatOfKeyPoint keyPoints = picture.getDescriptorProperty().getMatOfKeyPoint();
            if(keyPoints==null){
                picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture)); //**********************
                keyPoints = picture.getDescriptorProperty().getMatOfKeyPoint();
            }
            Mat descriptors = picture.getDescriptorProperty().getMatOfDescription();
            Mat outMat = new Mat();
            Mat imageMat = PictureWorker.getMatFromImage(FileWorker.getInstance().loadBufferedImage(picture.getPicFile()));
            double maxDistance = Double.MAX_VALUE;
            double distance;
            String bestGroup = "";
            List<List<Integer>> pointIdxsOfClusters = null;
            System.out.println("\n"+picture.getName());
            Mat mat = new Mat();
            for (Map.Entry<String, Mat> vocabularies : allTypeVocabularies.entrySet()) {
//                System.out.println(vocabularies.getKey());
                mat.push_back(vocabularies.getValue());
                System.out.println(vocabularies.getValue().cols()+"-"+vocabularies.getValue().rows());
//
            }
            System.out.println(mat.cols()+"-"+mat.rows());
//
//                extractor.setVocabulary(vocabularies.getValue());
                extractor.setVocabulary(mat);
                extractor.compute(descriptors, outMat, pointIdxsOfClusters);
                List<Double> ds = new ArrayList<>();
                for (int i = 0; i < outMat.rows(); i++) {
                    for (int j = 0; j < outMat.cols(); j++) {
                        distance = outMat.get(i, j)[0];
                        ds.add(distance);
//                        System.out.print(  Math.round(distance) + " ");
                    }
//                    System.out.println();
                }
                ds.stream().forEach(s-> {
//                    if (ds.indexOf(s) / 24 == 0) {
//                        System.out.println();
//                    }
                    System.out.print(Math.round(s) + " ");
                });
//                ds.sort(Double::compareTo);
                double adsdasd = ds.stream().sorted((o1, o2) -> -Double.compare(o1, o2)).mapToDouble(d -> d).limit(7).sum();
                if(adsdasd < maxDistance){
                    maxDistance=adsdasd;
//                    bestGroup= vocabularies.getKey();
                }

//                extractor.compute(imageMat, keyPoints, outMat, pointIdxsOfClusters, descriptors);
//                System.out.println();
//                for (int i = 0; i < outMat.rows(); i++) {
//                    for (int j = 0; j < outMat.cols(); j++) {
//                        System.out.print(outMat.get(i, j)[0] + " ");
//                    }
                    System.out.println("\n");
//                }
//            }
//            System.out.println("Best Group: " + bestGroup + " " + maxDistance);
//            picture.setExitPictureType(bestGroup);

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

//            picture.setPictureType(bestGroup);
        }
    }
}
