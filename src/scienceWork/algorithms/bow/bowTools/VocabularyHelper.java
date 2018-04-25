package scienceWork.algorithms.bow.bowTools;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
import scienceWork.objects.FeatureTypes;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.data.Vocabulary;

import java.util.List;

/**
 * Created by mixa1 on 18.03.2018.
 */
public class VocabularyHelper {
    private BOWTrainer bowTrainer;
    Progress progress;

    public VocabularyHelper(Progress progress) {
        this.progress = progress;
//        TermCriteria termCriteria = new TermCriteria(TermCriteria.MAX_ITER, 10000, 0.001);
        bowTrainer = new BOWKMeansTrainer(Settings.getCountBOWClusters());

//        BOWKMeansTrainer( int clusterCount, const TermCriteria& termcrit=TermCriteria(),
//        int attempts=3, int flags=KMEANS_PP_CENTERS );
    }

    public void createVocabulary(List<List<Picture>> pictLists) {
        int countPhotos = pictLists.stream().mapToInt(List::size).sum();
        for (List<Picture> pictList : pictLists) {
            addDescriptorsToBowTrainer(pictList);
        }
        System.out.println("bowTrainer " + bowTrainer.size);
        progress.addMessage("Start BOW clustering from " + bowTrainer.size + " descriptors");
        Mat vacMat = getCommonVocabulary();
//        BOWVocabulary.commonVocabulary = vacMat;
        BOWVocabulary.vocabulary = new Vocabulary(
                vacMat.total(),
                vacMat.rows(),
                vacMat.cols(),
                vacMat.type(),
                FeatureTypes.getFeatureId(Settings.getMethodKP()),
                Settings.getCountClusters(),
                countPhotos,
                bowTrainer.size,
                vacMat);
        progress.addMessage("Vocabulary was created " + Settings.getMethod() + "; size " + Settings.getCountClusters());
    }

    private void addDescriptorsToBowTrainer(List<Picture> pictureList) {
        long countPictures = pictureList.size();
        long count = 0;
        for (Picture picture : pictureList) {
            progress.setProgress(count++, countPictures);
            try {
                picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture)); //**********************

            Mat descriptor = picture.getDescriptorProperty().getMatOfDescription();
            System.out.println(picture.getName()+" "+picture.getDimension());
            bowTrainer.add(descriptor);
            } catch (Exception e) {
                System.out.println(picture.toString());
                e.printStackTrace();
            }
        }
        progress.setProgress(0, countPictures);
    }

    private Mat getCommonVocabulary() {
        return bowTrainer.cluster();
    }
}
