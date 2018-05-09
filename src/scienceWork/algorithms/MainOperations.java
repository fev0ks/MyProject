package scienceWork.algorithms;

import org.apache.commons.lang.time.StopWatch;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.Workers.FileWorker;
import scienceWork.algorithms.bow.BOWClusterer;
import scienceWork.algorithms.bow.BOWTeacher;
import scienceWork.algorithms.bow.VocabularyHelper.VocabularyWorker;
import scienceWork.objects.CommonML.AlgorithmML;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.data.Vocabulary;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by mixa1 on 04.04.2018.
 */
public class MainOperations {

    public void executeTraining(List<List<Picture>> pictLists, Progress progress) {
        StopWatch stopWatchAll = new StopWatch();
        stopWatchAll.start();
        System.out.println(pictLists.size());
        for (List<Picture> pictList : pictLists) {
//            System.out.println("analysis " + pictList.size());
//            System.out.println("analysis " + pictList.get(0).toString());
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            new BOWTeacher(pictList, progress).findFeatures();
            stopWatch.stop();
            viewWorkTime(stopWatch.getTime(), pictList.get(0).getPictureType(), progress);
        }
//        executeInitClassifier(progress);
        stopWatchAll.stop();
        viewWorkTime(stopWatchAll.getTime(), "Total teach ", progress);
    }

    public void executeInitClassifier(Progress progress, AlgorithmML classifier) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
//        SVM svm = SVM.load("E:\\YandexDisk\\JavaProject\\SDiplom\\123.xml");
//        svm.save("E:\\YandexDisk\\JavaProject\\SDiplom\\123ssss.xml");
//        classifier.setInstance(SVM.load("E:\\YandexDisk\\JavaProject\\SDiplom\\123.xml"));
//        try {
//            classifier.intiTrainData();
//        } catch (VocabularyNotFoundException e) {
//            e.printStackTrace();
//        }
//        classifier.getInstance(SVM.load("E:\\YandexDisk\\JavaProject\\SDiplom\\123.xml"));
        classifier.train();
        stopWatch.stop();

        System.out.println("Finish SVM ");
        long finishTime = stopWatch.getTime();
        System.out.println("Время: " + finishTime / 1000 + " сек, " + finishTime % 1000 + " мс;");


//        Mat mat = BOWVocabulary.vocabularies.get("forest");
//                long[][] newMat = new long[mat.rows()][mat.cols()];
//        for (int i = 0; i < mat.rows(); i++) {
//            for (int j = 0; j < mat.cols(); j++) {
//                System.out.print(Math.round(mat.get(i, j)[0]) + " ");
////                newMat[i][j] = Math.round(mat.get(i, j)[0]);
//            }
//            System.out.println();
//        }
//        System.out.println(Arrays.deepToString(newMat));
//        T svm = classifier.getInstance();
//
//        System.out.println("getClassWeights\n");
//        Mat mat = svm.getClassWeights();
////        long[][] newMat = new long[mat.rows()][mat.cols()];
//        for (int i = 0; i < mat.rows(); i++) {
//            for (int j = 0; j < mat.cols(); j++) {
//                System.out.print(Math.round(mat.get(i, j)[0]) + " ");
////                newMat[i][j] = Math.round(mat.get(i, j)[0]);
//            }
//            System.out.println();
//        }
//        System.out.println(Arrays.deepToString(newMat));

        viewWorkTime(stopWatch.getTime(), "SVM train ", progress);

        SaveDataHelper.saveClassifier(classifier, progress);

    }

//    public void showMat(Mat mat) {
//        long[][] newMat = new long[mat.rows()][mat.cols()];
//        for (int i = 0; i < mat.rows(); i++) {
//            for (int j = 0; j < mat.cols(); j++) {
////                System.out.print(Math.round(mat.get(i, j)[0]) + " ");
//                newMat[i][j] = Math.round(mat.get(i, j)[0]);
//            }
////            System.out.println();
//        }
//        System.out.println(Arrays.deepToString(newMat));
//    }

    public void executeClustering(List<List<Picture>> pictLists, Progress progress, AlgorithmML classifier) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            for (List<Picture> pictList : pictLists) {
                new BOWClusterer(pictList, progress, classifier).findPictureType(pictList);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        stopWatch.stop();
        viewWorkTime(stopWatch.getTime(), "Grouping", progress);

        checkResults(pictLists, progress);

    }

    private void checkResults(List<List<Picture>> pictLists, Progress progress) {
        int countPhotos = pictLists.stream().mapToInt(List::size).sum();
        DecimalFormat formatter = new DecimalFormat("#0.00");
        for (List<Picture> pictures : pictLists) {
            int countPictureInGroup = pictures.size();
            long right = pictures.stream().filter(p -> p.getPictureType().equals(p.getExitPictureType())).count();
            String message = pictures.get(0).getPictureType() + ": " + "Right: " + right + "; False: " + (countPictureInGroup - right) + "; Rate: " + formatter.format((double) right / countPictureInGroup * 100) + "%";
            progress.addMessage(message);
        }
        long right = pictLists.stream().map(list -> list.stream().filter(p -> p.getPictureType().equals(p.getExitPictureType())).count()).mapToLong(s -> s).sum();
        String message = "Total: Right: " + right + "; False: " + (countPhotos - right) + "; Rate: " + formatter.format((double) right / countPhotos * 100) + "%";
        progress.addMessage(message);
    }

    public void executeInitVocabulary(List<List<Picture>> pictLists, Progress progress) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        new VocabularyWorker(progress).createVocabulary(pictLists);
        stopWatch.stop();
        Vocabulary vocabulary = BOWVocabulary.vocabulary;
        viewWorkTime(stopWatch.getTime(), "Vocabulary size " + vocabulary.getMat().size(), progress);

        SaveDataHelper.saveVocabulary(BOWVocabulary.vocabulary, progress);

    }

    public void executeSavingGroups(List<List<Picture>> pictureLists, File file, int countPhotos, Progress progress) {
        if (file == null) {
            progress.addMessage("Pictures will be saved to default folder: " + pictureLists.get(0).get(0).getPicFile().getParent());
        }
        int count = 0;
        try {
            for (List<Picture> pictures : pictureLists)
                for (Picture picture : pictures) {
                    FileWorker.getInstance().saveImageToGroups(picture, file);
                    progress.setProgress(++count, countPhotos);
                }
            progress.setProgress(0, countPhotos);
            progress.addMessage("\nImages were saved\n");
        } catch (IOException e) {
            e.printStackTrace();
            progress.addMessage("Failed to save pictures!\n");
        }
    }

    private void viewWorkTime(long finishTime, String title, Progress progress) {
        String message = title + "; Finished for " + finishTime / 1000 / 60 + " min, " + finishTime / 1000 % 60 + " sec, " + finishTime % 1000 + " ms;";
        progress.addMessage(message);
    }

}
