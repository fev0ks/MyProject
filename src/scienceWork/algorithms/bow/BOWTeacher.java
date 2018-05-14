package scienceWork.algorithms.bow;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
import scienceWork.algorithms.Interfaces.Teacher;
import scienceWork.algorithms.bow.bowTools.BOWImgDescriptorExtractor;
import scienceWork.objects.GeneralPicturesInformation;
import scienceWork.objects.Picture;
import scienceWork.objects.data.BOWVocabulary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mixa1 on 23.02.2018.
 */
public class BOWTeacher implements Teacher {
    //    private List<Picture> pictureList;
    private List<List<Picture>> pictureLists;
    private Progress progress;
    private BOWImgDescriptorExtractor extractor;
    private VocabularyTools vocabularyTools;

    //    public BOWTeacher(List<Picture> pictureList, Progress progress) {
    public BOWTeacher(List<List<Picture>> pictureLists, Progress progress) {
        this.pictureLists = pictureLists;
        this.progress = progress;
        this.extractor = BOWVocabulary.getBOWImgDescriptorExtractor();
        this.vocabularyTools = new VocabularyTools();
        BOWVocabulary.vocabularies = new HashMap<>();
    }

    @Override
    public void findFeatures() {
        executeCalculateGroupHistograms();
    }

    private void executeCalculateGroupHistograms() {
        long count = 0;
        int countPictures = GeneralPicturesInformation.getInstance().getPictureCount();
        BOWVocabulary.countUsedPictures = countPictures;
        for (List<Picture> pictureList : pictureLists) {
            extractor.setVocabulary(BOWVocabulary.vocabulary.getMat());
            Mat groupHistograms = new Mat();

            for (Picture picture : pictureList) {
                progress.setProgress(count++, countPictures);

                Mat pictureHist = vocabularyTools.getPictureHistogram(picture, extractor);
                if (pictureHist != null) {
                    groupHistograms.push_back(pictureHist);
                }
            }

            String pictureType = pictureList.get(0).getPictureType();
            progress.addMessage("Label: "+pictureType);
            BOWVocabulary.vocabularies.put(pictureType, groupHistograms);
        }
        progress.setProgress(0, countPictures);
    }

//    private Mat executeCalculateGroupHistograms(Picture picture) {
//
//        GeneralPicturesInformation.getInstance().clearKPData();
//
//
//    }
//
//        return groupHistograms;
//}


}
