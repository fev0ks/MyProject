package scienceWork.algorithms.bow;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
import scienceWork.algorithms.Interfaces.Clusterer;
import scienceWork.algorithms.bow.bowTools.BOWImgDescriptorExtractor;
import scienceWork.objects.CommonML.AlgorithmML;
import scienceWork.objects.Picture;
import scienceWork.objects.data.BOWVocabulary;

import java.util.List;

import static org.opencv.core.CvType.CV_32F;

/**
 * Created by mixa1 on 23.02.2018.
 */
public class BOWClusterer implements Clusterer {
    private Progress progress;
    private AlgorithmML classifier;
    private List<Picture> pictureList;
    private BOWImgDescriptorExtractor extractor;

    public BOWClusterer(List<Picture> pictureList, Progress progress, AlgorithmML classifier) {
        this.extractor = BOWVocabulary.getBOWImgDescriptorExtractor();
        extractor.setVocabulary(BOWVocabulary.vocabulary.getVocabulary());
        this.pictureList = pictureList;
        this.progress = progress;
        this.classifier = classifier;
//        System.out.println("getVarCount "+svm.getVarCount());
    }

    @Override
    public void findPictureType(List<Picture> pictureList) {
        execute();
    }

    private void execute() {
        long countPictures = pictureList.size();
        long count = 0;
        for (Picture picture : pictureList) {
            progress.setProgress(count++, countPictures);
            float prediction;
            Mat vocabularyTemp = findVocabularyTemp(picture);
            vocabularyTemp.convertTo(vocabularyTemp, CV_32F);
            if (vocabularyTemp.empty()) {
                picture.setExitPictureType("failed");
                continue;
            }
            prediction = classifier.predict(vocabularyTemp);
            System.out.println(picture.getPictureType() + ": prediction " + prediction);
            String typeImage = BOWVocabulary.classesNumbers.get((int) prediction);
            picture.setExitPictureType(typeImage);
        }
        progress.setProgress(0, countPictures);
    }

    private Mat findVocabularyTemp(Picture picture) {
        Mat outMat = new Mat();
        MatOfKeyPoint keyPoints = picture.getDescriptorProperty().getMatOfKeyPoint();
        try {
            if (keyPoints == null) {
                picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture)); //**********************
            }
            Mat descriptors = picture.getDescriptorProperty().getMatOfDescription();
            List<List<Integer>> pointIdxsOfClusters = null;
            extractor.compute(descriptors, outMat, pointIdxsOfClusters);
        } catch (Exception e) {
            System.out.println(picture.toString());
            e.printStackTrace();
        }
        return outMat;
    }
}
