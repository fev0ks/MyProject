package scienceWork.algorithms;

import org.apache.commons.lang.time.StopWatch;
import org.opencv.core.Mat;
import org.opencv.ml.SVM;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.bow.BOWClusterer;
import scienceWork.algorithms.bow.BOWTeacher;
import scienceWork.algorithms.bow.bowTools.VocabularyHelper;
import scienceWork.objects.Picture;
import scienceWork.objects.SVMInstance;
import scienceWork.objects.picTypesData.BOWVocabulary;

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
//        executeInitSVM(progress);
        stopWatchAll.stop();
        viewWorkTime(stopWatchAll.getTime(), "Total teach ", progress);
    }

    public void executeInitSVM(Progress progress) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SVMInstance.getSVMInstance().trainSVM();
        stopWatch.stop();
        System.out.println("Finish SVM ");
        long finishTime = stopWatch.getTime();
        System.out.println("Время: " + finishTime / 1000 + " сек, " + finishTime % 1000 + " мс;");
        SVM svm = SVMInstance.getSVMInstance().getSvm();
        System.out.println(
                "getType " + svm.getType() +
                        " \ngetKernelType " + svm.getKernelType() +
                        " \ngetVarCount " + svm.getVarCount() +
                        " \ngetC " + svm.getC() +
                        " \ngetCoef0 " + svm.getCoef0() +
                        " \ngetDegree " + svm.getDegree() +
                        " \ngetGamma " + svm.getGamma() +
                        " \ngetNu " + svm.getNu() +
                        " \ngetP " + svm.getP() +
//                        " \ngetDefaultName " + svm.getDefaultName()+
                        " \ngetTermCriteria " + svm.getTermCriteria().type+
                        " \nmaxCount " + svm.getTermCriteria().maxCount+
                        " \nepsilon " + svm.getTermCriteria().epsilon
        );
        System.out.println("getClassWeights\n");
        Mat mat = svm.getClassWeights();
//        long[][] newMat = new long[mat.rows()][mat.cols()];
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                System.out.print(Math.round(mat.get(i, j)[0]) + " ");
//                newMat[i][j] = Math.round(mat.get(i, j)[0]);
            }
            System.out.println();
        }
//        System.out.println(Arrays.deepToString(newMat));

       viewWorkTime(stopWatch.getTime(), "SVM train ", progress);
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

    public void executeClustering(List<List<Picture>> pictLists, Progress progress) {
        int count = 0;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            for (List<Picture> pictList : pictLists) {
                new BOWClusterer(pictList, progress).findPictureType(pictList);
                count += pictList.size();
            }
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        stopWatch.stop();
        viewWorkTime(stopWatch.getTime(), "Grouping", progress);
        long right = pictLists.stream().map(list -> list.stream().filter(p -> p.getPictureType().equals(p.getExitPictureType())).count()).mapToLong(s -> s).sum();
        progress.addMessage("Right: " + right + "; False: " + (count - right) + "; Rate: " + ((double) right / count * 100) + "%");
        System.out.println("Right: " + right + "; False: " + (count - right) + "; Rate: " + ((double) right / count * 100) + "%");
    }

    public void executeInitVocabulary(List<List<Picture>> pictLists, Progress progress) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        new VocabularyHelper(progress).createVocabulary(pictLists);
        stopWatch.stop();
        viewWorkTime(stopWatch.getTime(), "Vocabulary size " + BOWVocabulary.commonVocabulary.size(), progress);
    }

    private void viewWorkTime(long finishTime, String title, Progress progress) {
        System.out.println(title + "; Время: " + finishTime / 1000 + " сек, " + finishTime % 1000 + " мс;");
        String message = "Завершено за " + finishTime / 1000 + " сек, " + finishTime % 1000 + " мс;";
        progress.addMessage(title + "; " + message);
    }

}