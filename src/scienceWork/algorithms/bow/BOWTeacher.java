package scienceWork.algorithms.bow;

import org.opencv.core.Mat;
import org.opencv.features2d.BOWKMeansTrainer;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.Interfaces.Teacher;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.picTypesData.BOWVocabulary;

import java.util.List;

/**
 * Created by mixa1 on 23.02.2018.
 */
public class BOWTeacher implements Teacher {
    private List<Picture> pictureList;
    private Progress progress;
    private Mat vocabulary;

    public BOWTeacher(List<Picture> pictureList, Progress progress) {
        this.pictureList = pictureList;
        this.progress = progress;
        BOWKMeansTrainer bowTrainer = new BOWKMeansTrainer(Settings.getCountBOWClusters());
        addDescriptorsToBowTrainer(bowTrainer);
        this.vocabulary = bowTrainer.cluster();

    }

    @Override
    //Вычисляю для заданного типа изображений их особенности
    public void findFeatures() {
        String pictureType = pictureList.get(0).getPictureType();
        if (vocabulary.height() != 0)
            BOWVocabulary.vocabularies.put(pictureType, vocabulary);
    }

    private void addDescriptorsToBowTrainer(BOWKMeansTrainer bowTrainer) {
        for (Picture picture : pictureList) {
            Mat descriptor = picture.getDescriptorProperty().getMatOfDescription();
            bowTrainer.add(descriptor);
        }
    }
}
