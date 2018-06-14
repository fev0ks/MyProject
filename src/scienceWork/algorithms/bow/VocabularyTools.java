package scienceWork.algorithms.bow;

import org.opencv.core.Mat;
import scienceWork.MainOperations;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
import scienceWork.algorithms.bow.bowTools.BOWImgDescriptorExtractor;
import scienceWork.objects.GeneralPicturesInformation;
import scienceWork.objects.Picture;
import scienceWork.objects.data.BOWVocabulary;

import java.util.List;
import java.util.Map;

public class VocabularyTools {

    private final int avgKeyPoints = 0;
    private final int difMaxMinCountKeyPoints = 0;

    public VocabularyTools() {
//        GeneralPicturesInformation generalPicturesInfo = GeneralPicturesInformation.getInstance();
//
//        int countKeyPoints = generalPicturesInfo.getCountKeyPoints();
//        avgKeyPoints = countKeyPoints / BOWVocabulary.countUsedPictures;
//
//        int minKeyPoints = generalPicturesInfo.getMinKeyPoints();
//        int maxKeyPoints = generalPicturesInfo.getMaxKeyPoints();
//        difMaxMinCountKeyPoints = maxKeyPoints - minKeyPoints;
    }

    public void normalizeVocabulary() {
        for (Map.Entry<String, Mat> entrySet : BOWVocabulary.vocabularies.entrySet()) {
            Mat mat = entrySet.getValue();
            normalizeBOWMat(mat);
        }

    }

    public void normalizeBOWMat(Mat mat) {
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                double normalizedValue = (mat.get(i, j)[0] - avgKeyPoints) / difMaxMinCountKeyPoints;
                System.out.println("val: "+mat.get(i, j)[0]+" avgKeyPoints: "+ avgKeyPoints+" difMaxMinCountKeyPoints: "+ difMaxMinCountKeyPoints+ " keks: "+normalizedValue);
                mat.put(i, j, normalizedValue);
            }
        }
//        new MainOperations().showMat(mat);
    }

    public Mat getPictureHistogram(Picture picture, BOWImgDescriptorExtractor extractor){
        Mat histogram = new Mat();
        try {
            picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture));
//                    GeneralPicturesInformation.getInstance().updateGeneralInformation(picture);

            Mat descriptors = picture.getDescriptorProperty().getMatOfDescription();

            List<List<Integer>> pointIdxsOfClusters = null;
            extractor.compute(descriptors, histogram, pointIdxsOfClusters);
            picture.setDescriptorProperty(null);
            System.gc();
//                    outMat = normalizeByRows(outMat);

        } catch (Exception e) {
            System.out.println(picture.toString());
            e.printStackTrace();
        }
        return histogram;
    }
}
