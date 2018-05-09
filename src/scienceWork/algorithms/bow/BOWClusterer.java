package scienceWork.algorithms.bow;

import org.opencv.core.Mat;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.Workers.FileWorker;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
import scienceWork.algorithms.Interfaces.Clusterer;
import scienceWork.algorithms.bow.bowTools.BOWImgDescriptorExtractor;
import scienceWork.objects.CommonML.AlgorithmML;
import scienceWork.objects.Picture;
import scienceWork.objects.data.BOWVocabulary;

import java.util.Arrays;
import java.util.List;

import static org.opencv.core.CvType.CV_32F;

/**
 * Created by mixa1 on 23.02.2018.
 */
public class BOWClusterer implements Clusterer {
    private Progress progress;
    private AlgorithmML classifier;
    private List<Picture> pictureList;
    private BOWImgDescriptorExtractor extractor;

    public BOWClusterer(List<Picture> pictureList, Progress progress, AlgorithmML classifier) {
        this.extractor = BOWVocabulary.getBOWImgDescriptorExtractor();
        extractor.setVocabulary(BOWVocabulary.vocabulary.getMat());
        this.pictureList = pictureList;
        this.progress = progress;
        this.classifier = classifier;
//        System.out.println("getVarCount "+svm.getVarCount());
    }

    @Override
    public void findPictureType(List<Picture> pictureList) {
        execute();
    }

    private void execute() {
        long countPictures = pictureList.size();
        long count = 0;
        for (Picture picture : pictureList) {
//
            progress.setProgress(count++, countPictures);
            float prediction;
            Mat vocabularyTemp = findVocabularyTemp(picture);
            vocabularyTemp.convertTo(vocabularyTemp, CV_32F);
            if (vocabularyTemp.empty()) {
                picture.setExitPictureType("failed");
                continue;
            }
            prediction = classifier.predict(vocabularyTemp);
            System.out.println(count + "\\" + countPictures + " " + picture.getPictureType() + " prediction " + prediction);
            String typeImage = BOWVocabulary.classesNumbers.get((int) prediction);
            picture.setExitPictureType(typeImage);
            picture.setDescriptorProperty(null);
        }
        progress.setProgress(0, countPictures);
    }

    private Mat findVocabularyTemp(Picture picture) {
        Mat outMat = new Mat();
//        MatOfKeyPoint keyPoints = picture.getDescriptorProperty().getMatOfKeyPoint();
        try {
//            if (keyPoints == null) {
            picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture)); //**********************
//            }
            Mat descriptors = picture.getDescriptorProperty().getMatOfDescription();
            List<List<Integer>> pointIdxsOfClusters = null;
            extractor.compute(descriptors, outMat, pointIdxsOfClusters);
            picture.setDescriptorProperty(null);
//            outMat = normalizeByRows(outMat);
//            System.gc();
        } catch (Exception e) {
            System.out.println(picture.toString());
            e.printStackTrace();
        }
        return outMat;
    }

    private Mat normalizeByRows(Mat mat) {
        double normalize = mat.cols();
        for (int i = 0; i < normalize; i++) {
            mat.put(0, i, (double)(mat.get(0, i)[0]/normalize));
        }
//        showMat(mat);
        return mat;
    }

    public void showMat(Mat mat) {
//        long[][] newMat = new long[mat.rows()][mat.cols()];
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                System.out.print(mat.get(i, j)[0] + " ");
//                newMat[i][j] = Math.round(mat.get(i, j)[0]);
            }
            System.out.println();
        }
//        System.out.println(Arrays.deepToString(newMat));
    }
}
