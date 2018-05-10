package scienceWork.algorithms.bow.VocabularyHelper;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
import scienceWork.algorithms.bow.bowTools.BOWKMeansTrainer;
import scienceWork.algorithms.bow.bowTools.BOWTrainer;
import scienceWork.objects.FeatureTypes;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.data.Vocabulary;

import java.util.List;

/**
 * Created by mixa1 on 18.03.2018.
 */
public class VocabularyCreator {
    private BOWTrainer bowTrainer;
    Progress progress;

    public VocabularyCreator(Progress progress) {
        this.progress = progress;
//        TermCriteria termCriteria = new TermCriteria(TermCriteria.MAX_ITER, 10000, 0.001);
        bowTrainer = new BOWKMeansTrainer(Settings.getCountBOWClusters());

//    public BOWKMeansTrainer(int clusterCount, TermCriteria termCriteria, int attempts, int flags) {
//            this.clusterCount = clusterCount;
//            this.termCriteria = termCriteria;
//            this.attempts = attempts;
//            this.flags = flags;
//        }
//        KMEANS_RANDOM_CENTERS Select random initial centers in each attempt.
//        KMEANS_PP_CENTERS Use kmeans++ center initialization by Arthur and Vassilvitskii [Arthur2007].
//        KMEANS_USE_INITIAL_LABELS During the first (and possibly the only) attempt, use the user-supplied labels instead of computing them from the initial centers. For the second and further attempts, use the random or semi-random centers. Use one of KMEANS_*_CENTERS flag to specify the exact method.
//        BOWKMeansTrainer( int clusterCount, const TermCriteria& termcrit=TermCriteria(),
//        int attempts=3, int flags=KMEANS_PP_CENTERS );
    }

    public void createVocabulary(List<List<Picture>> pictLists) {
        int countPhotos = pictLists.stream().mapToInt(List::size).sum();
        for (List<Picture> pictList : pictLists) {
            addDescriptorsToBowTrainer(pictList);
        }
        progress.addMessage("Start BOW clustering from " + bowTrainer.descriptorsCount() + " descriptors; size " + Settings.getCountClusters() + ";");
        Mat vacMat = getCommonVocabulary();
//        BOWVocabulary.commonVocabulary = vacMat;
        BOWVocabulary.vocabulary = new Vocabulary(
                vacMat.rows(),
                vacMat.cols(),
                vacMat.type(),
                FeatureTypes.getFeatureId(Settings.getMethodKP()),
                Settings.getCountClusters(),
                countPhotos,
                bowTrainer.descriptorsCount(),
                vacMat,
                pictLists.size());
        progress.addMessage("Vocabulary was created " + FeatureTypes.getLabel(Settings.getMethodKP())+ ";");
    }

    private void addDescriptorsToBowTrainer(List<Picture> pictureList) {
        long countPictures = pictureList.size();
        long count = 0;
        for (Picture picture : pictureList) {
            progress.setProgress(count++, countPictures);
            try {
                picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture));
                Mat descriptor = picture.getDescriptorProperty().getMatOfDescription();
                System.out.println(picture.getName() + " " + picture.getDimension() + "; desc: " + descriptor.size());
                bowTrainer.add(descriptor);
                picture.setDescriptorProperty(null);
                System.gc();
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
