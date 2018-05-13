package scienceWork;

import javafx.scene.control.Alert;
import org.apache.commons.lang.time.StopWatch;
import org.opencv.core.Mat;
import scienceWork.FxWorker.FxHelper;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.Workers.FileWorker;
import scienceWork.algorithms.bow.BOWClusterer;
import scienceWork.algorithms.bow.BOWTeacher;
import scienceWork.algorithms.bow.bowTools.VocabularyCreator;
import scienceWork.dataBase.SaveDataHelper;
import scienceWork.objects.GeneralPicturesInformation;
import scienceWork.objects.machineLearning.CommonML.AlgorithmML;
import scienceWork.objects.Picture;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.data.Vocabulary;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by mixa1 on 04.04.2018.
 */
public class MainOperations {

    public void executeTraining(List<List<Picture>> pictLists, Progress progress) {
        StopWatch stopWatchAll = new StopWatch();
        stopWatchAll.start();
        System.out.println(pictLists.size());
//        for (List<Picture> pictList : pictLists) {
//            System.out.println("analysis " + pictList.size());
//            System.out.println("analysis " + pictList.get(0).toString());
//            StopWatch stopWatch = new StopWatch();
//            stopWatch.start();
        new BOWTeacher(pictLists, progress).findFeatures();
//        normalizeVocabularyData(progress);

//            stopWatch.stop();
//            viewWorkTime(stopWatch.getTime(), pictList.get(0).getPictureType(), progress);
//        }
//        executeInitClassifier(progress);
        stopWatchAll.stop();
        viewWorkTime(stopWatchAll.getTime(), "Total time that was spent on creating training data:", progress);
    }

//    private void normalizeVocabularyData(Progress progress) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        new VocabularyTools().normalizeVocabulary();
//        stopWatch.stop();
//        viewWorkTime(stopWatch.getTime(), "Total time that was spent on normalization data:", progress);
//    }

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

//        System.out.println("Finish");
//        long finishTime = stopWatch.getTime();

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

        viewWorkTime(stopWatch.getTime(), "Classifier trained ", progress);

        SaveDataHelper.saveClassifier(classifier, progress);

    }

    public void executeClustering(List<List<Picture>> pictLists, Progress progress, AlgorithmML classifier) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
//            for (List<Picture> pictList : pictLists) {
            new BOWClusterer(pictLists, progress, classifier).findPictureType();
//            }
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
        new VocabularyCreator(progress).createVocabulary(pictLists);
        stopWatch.stop();
        Vocabulary vocabulary = BOWVocabulary.vocabulary;
        viewWorkTime(stopWatch.getTime(), "Vocabulary size " + vocabulary.getMat().size(), progress);

        SaveDataHelper.saveVocabulary(BOWVocabulary.vocabulary, progress);

    }

    public void executeSavingGroups(List<List<Picture>> pictureLists, File file, Progress progress) {

        int count = 0;
        int notSavedCount = 0;
        int countPictures = GeneralPicturesInformation.getInstance().getPictureCount();
        long createdDate = Calendar.getInstance().getTimeInMillis();
        String temp = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdDate),
                TimeZone.getDefault().toZoneId()).format(DateTimeFormatter.ofPattern("dd_MM_yyyy H_m_s"));

        try {
            for (List<Picture> pictures : pictureLists)
                for (Picture picture : pictures) {
                    if (checkPictureType(picture)) {
                        FileWorker.getInstance().saveImageToGroups(picture, file, temp);
                        progress.setProgress(++count, countPictures);
                    } else {
                        notSavedCount++;
                    }
                }
            progress.setProgress(0, countPictures);
            progress.addMessage("* Images were saved: " + (countPictures - notSavedCount) + "; not saved: " + notSavedCount+ " *");
        } catch (IOException e) {
            e.printStackTrace();
            progress.addMessage("* Failed to save pictures! *");
        }
    }

    private boolean checkPictureType(Picture picture) {
        String pictureType = picture.getExitPictureType();
        return pictureType != null && !pictureType.equals("");
    }

    private void viewWorkTime(long finishTime, String title, Progress progress) {
        String message = title + " " + finishTime / 1000 / 60 + " min, " + finishTime / 1000 % 60 + " sec, " + finishTime % 1000 + " ms;";
        progress.addMessage(message);
    }

    public boolean checkExistVocabulary() {
        boolean exist = true;
        if (BOWVocabulary.vocabulary.getMat() == null) {
            FxHelper.showMessage("Warning", "Vocabulary is empty!", "Please load or create", Alert.AlertType.WARNING, new Main());
            exist = false;
        }
        return exist;
    }

    public boolean checkExistTrainData() {
        boolean exist = true;
        if (BOWVocabulary.vocabularies.isEmpty()) {
            FxHelper.showMessage("Warning", "Train data is empty!", "Please create", Alert.AlertType.WARNING, new Main());
            exist = false;
        }
        return exist;
    }

    public boolean checkExistMLInstance(AlgorithmML algorithmML) {
        boolean exist = true;
        if (algorithmML == null) {
            FxHelper.showMessage("Warning", "Classifier Instance is not exist!", "Please create", Alert.AlertType.WARNING, new Main());
            exist = false;
        }
        return exist;
    }

    public boolean checkLoadedPictures(List<List<Picture>> pictureLists) {
        boolean exist = true;
        if (pictureLists == null) {
            FxHelper.showMessage("Warning", "Pictures were not loaded yet", "Please wait", Alert.AlertType.WARNING, new Main());
            exist = false;
        }
        return exist;
    }

    public void showMat(Mat mat) {
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                System.out.print((mat.get(i, j)[0]) + " ");
            }
            System.out.println();
        }
    }

}
