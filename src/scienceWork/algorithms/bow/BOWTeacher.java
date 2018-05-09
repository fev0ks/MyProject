package scienceWork.algorithms.bow;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.Workers.FileWorker;
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

    @Override
    public void findFeatures() {
        execute();
    }

    private void execute() {
        String pictureType = pictureList.get(0).getPictureType();
        BOWVocabulary.vocabularies.put(pictureType, getGroupHistograms());
    }

    private Mat getGroupHistograms() {
        extractor.setVocabulary(BOWVocabulary.vocabulary.getMat());
        Mat groupHistograms = new Mat();
        long countPictures = pictureList.size();
        long count = 0;
        for (Picture picture : pictureList) {

            progress.setProgress(count++, countPictures);
            try {
                picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture));
                Mat descriptors = picture.getDescriptorProperty().getMatOfDescription();
                Mat outMat = new Mat();
                List<List<Integer>> pointIdxsOfClusters = null;
                extractor.compute(descriptors, outMat, pointIdxsOfClusters);
                picture.setDescriptorProperty(null);
//                outMat = normalizeByRows(outMat);
                groupHistograms.push_back(outMat);
            } catch (Exception e) {
                System.out.println(picture.toString());
                e.printStackTrace();
            }

        }
        System.out.println("groupHistograms " + groupHistograms.size());
        progress.setProgress(0, countPictures);
        return groupHistograms;
    }

    private Mat normalizeByRows(Mat mat) {
        double normalize = mat.cols();
        for (int i = 0; i < normalize; i++) {
            mat.put(0, i, (double)(mat.get(0, i)[0]/normalize));
        }
//        showMat(mat);
        return mat;
    }
}
