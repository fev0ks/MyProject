package scienceWork.algorithms.bow;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
import scienceWork.algorithms.Interfaces.Clusterer;
import scienceWork.algorithms.bow.bowTools.BOWImgDescriptorExtractor;
import scienceWork.objects.GeneralPicturesInformation;
import scienceWork.objects.machineLearning.CommonML.AlgorithmML;
import scienceWork.objects.Picture;
import scienceWork.objects.data.BOWVocabulary;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.opencv.core.CvType.CV_32F;

/**
 * Created by mixa1 on 23.02.2018.
 */
public class BOWClusterer implements Clusterer {
    private Progress progress;
    private AlgorithmML classifier;
    private List<List<Picture>> pictureLists;
    private BOWImgDescriptorExtractor extractor;
    private VocabularyTools vocabularyTools;

    public BOWClusterer(List<List<Picture>> pictureLists, Progress progress, AlgorithmML classifier) {

        this.pictureLists = pictureLists;
        this.progress = progress;
        this.classifier = classifier;
        this.vocabularyTools = new VocabularyTools();
    }

    @Override
    public void findPictureType() {

        classifiedPictures();
    }

    private void classifiedPictures() {
        long countPictures = GeneralPicturesInformation.getInstance().getPictureCount();
        long count = 0;
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        List<Future> futureList = new LinkedList<>();

        for (List<Picture> pictureList : pictureLists) {


            for (Picture picture : pictureList) {
                extractor = BOWVocabulary.getBOWImgDescriptorExtractor();
                extractor.setVocabulary(BOWVocabulary.vocabulary.getMat());
//                extractor = BOWVocabulary.getBOWImgDescriptorExtractor();
//                extractor.setVocabulary(BOWVocabulary.vocabulary.getMat());

                executeCalculatePictureType(picture);
                progress.setProgress(count++, countPictures);
//                Thread thread = new Thread(() -> executeCalculatePictureType(picture));
//                futureList.add(executor.submit(thread));
//                thread.start();


            }

        }
//
//        System.out.println(" " + futureList.size());
//            for (int i = 0; i < countPictures; i++) {
//                System.out.println(" " +  futureList.get(i).isDone());
//                try {
//                    futureList.get(i).get();
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//
//                }

//            }

//        executor.shutdown();
        progress.setProgress(0, countPictures);
    }

    private void executeCalculatePictureType(Picture picture) {
        float prediction = -1;
        Mat vocabularyTemp = vocabularyTools.getPictureHistogram(picture, extractor);
        vocabularyTemp.convertTo(vocabularyTemp, CV_32F);
        try {
            prediction = classifier.predict(vocabularyTemp);
            System.out.println(" pred- " + prediction + " " + picture.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (prediction != -1) {
            try {
                String typeImage = BOWVocabulary.classesNumbers.get((int) prediction);
                picture.setExitPictureType(typeImage);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        } else {
            picture.setExitPictureType("< empty >");
        }

    }
}
