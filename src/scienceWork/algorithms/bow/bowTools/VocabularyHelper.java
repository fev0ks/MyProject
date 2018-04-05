package scienceWork.algorithms.bow.bowTools;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.picTypesData.BOWVocabulary;

import java.util.List;

/**
 * Created by mixa1 on 18.03.2018.
 */
public class VocabularyHelper {
    private BOWTrainer bowTrainer;
    Progress progress;

    public VocabularyHelper(Progress progress) {
        this.progress = progress;
        bowTrainer = new BOWKMeansTrainer(Settings.getCountBOWClusters());
    }

    public void createVocabulary(List<List<Picture>> pictLists) {
        for (List<Picture> pictList : pictLists) {
            addDescriptorsToBowTrainer(pictList);
        }
        System.out.println("bowTrainer "+bowTrainer.size);
        progress.addMessage("Start BOW clustering from "+bowTrainer.size+ " descriptors");
        BOWVocabulary.commonVocabulary = getCommonVocabulary();
        System.out.println("commonVocabulary "+BOWVocabulary.commonVocabulary.size());
    }

    private void addDescriptorsToBowTrainer(List<Picture> pictureList) {
        long countPictures = pictureList.size();
        long count =0;
        for (Picture picture : pictureList) {
            progress.setProgress(count++,countPictures);
            try {
                picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture)); //**********************
            } catch(Exception e){
                continue;
            }
            Mat descriptor = picture.getDescriptorProperty().getMatOfDescription();
            bowTrainer.add(descriptor);
        }
        progress.setProgress(0 ,countPictures);
    }

    private Mat getCommonVocabulary() {
        return bowTrainer.cluster();
    }
}
