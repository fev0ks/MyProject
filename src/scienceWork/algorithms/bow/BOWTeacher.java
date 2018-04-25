package scienceWork.algorithms.bow;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
import scienceWork.algorithms.Interfaces.Teacher;
import scienceWork.algorithms.bow.bowTools.BOWImgDescriptorExtractor;
import scienceWork.objects.Picture;
import scienceWork.objects.data.BOWVocabulary;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mixa1 on 23.02.2018.
 */
public class BOWTeacher implements Teacher {
    private List<Picture> pictureList;
    private Progress progress;
    private BOWImgDescriptorExtractor extractor;

    public BOWTeacher(List<Picture> pictureList, Progress progress) {
        this.pictureList = pictureList;
        this.progress = progress;
        this.extractor = BOWVocabulary.getBOWImgDescriptorExtractor();
    }

//    private BOWImgDescriptorExtractor getBOWImgDescriptorExtractor() {
//        DescriptorExtractor descriptor = DescriptorExtractor.create(Settings.getMethodDescr());
//        DescriptorMatcher matcher = DescriptorMatcher.create(Settings.getMethodMatcher());
//        return new BOWImgDescriptorExtractor(descriptor, matcher);
//    }

    @Override
    public void findFeatures() {
        execute();
    }

    private void execute() {
        String pictureType = pictureList.get(0).getPictureType();
        BOWVocabulary.vocabularies.put(pictureType, getGroupHistograms());
    }

    private Mat getGroupHistograms() {
        extractor.setVocabulary(BOWVocabulary.vocabulary.getVocabulary());
        Mat groupHistograms = new Mat();
        long countPictures = pictureList.size();
        long count = 0;
        for (Picture picture : pictureList) {
            progress.setProgress(count++, countPictures);
            MatOfKeyPoint keyPoints = picture.getDescriptorProperty().getMatOfKeyPoint();
            if (keyPoints == null) {
                try {
                    picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture)); //**********************
                }catch(Exception e){
                    continue;
                }
//                keyPoints = picture.getDescriptorProperty().getMatOfKeyPoint();
            }
            Mat descriptors = picture.getDescriptorProperty().getMatOfDescription();
            Mat outMat = new Mat();
//            Mat imageMat = PictureWorker.getMatFromImage(FileWorker.getInstance().loadBufferedImage(picture.getPicFile()));
            List<List<Integer>> pointIdxsOfClusters = null;
            System.out.println("\n" + picture.getName());
            extractor.compute(descriptors, outMat, pointIdxsOfClusters);
            groupHistograms.push_back(outMat);

        }
//        showMat(groupHistograms);
        System.out.println("groupHistograms " + groupHistograms.size());
        progress.setProgress(0, countPictures);
        return groupHistograms;
    }

    public void showMat(Mat mat) {
        long[][] newMat = new long[mat.rows()][mat.cols()];
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
//                System.out.print(Math.round(mat.get(i, j)[0]) + " ");
                newMat[i][j] = Math.round(mat.get(i, j)[0]);
            }
//            System.out.println();
        }
        System.out.println(Arrays.deepToString(newMat));
    }
}
