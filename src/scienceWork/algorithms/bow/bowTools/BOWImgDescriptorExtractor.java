package scienceWork.algorithms.bow.bowTools;

import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;

import java.util.*;

/**
 * Class to compute an image descriptor using the *bag of visual words*.
 * Such a computation consists of the following steps:
 * <p>
 * 1.  Compute descriptors for a given image and its keypoints set.
 * 2.  Find the nearest visual words from the vocabulary for each keypoint descriptor.
 * 3.  Compute the bag-of-words image descriptor as is a normalized histogram of vocabulary words
 * encountered in the image. The i-th bin of the histogram is a frequency of i-th word of the
 * vocabulary in the given image.
 */
public class BOWImgDescriptorExtractor {

    protected Mat vocabulary = new Mat();
    protected DescriptorExtractor dextractor;
    protected DescriptorMatcher dmatcher;

    /**
     * @param dextractor Descriptor extractor that is used to compute descriptors for an input image and
     *                   its keypoints.
     * @param dmatcher   Descriptor matcher that is used to find the nearest word of the trained vocabulary
     *                   for each keypoint descriptor of the image.
     */
    public BOWImgDescriptorExtractor(DescriptorExtractor dextractor, DescriptorMatcher dmatcher) {
        this.dextractor = dextractor;
        this.dmatcher = dmatcher;
    }

    public BOWImgDescriptorExtractor(DescriptorMatcher dmatcher) {
        this.dmatcher = dmatcher;
    }

    /**
     * Sets a visual vocabulary.
     *
     * @param vocabulary Vocabulary (can be trained using the inheritor of BOWTrainer ). Each row of the
     *                   vocabulary is a visual word (cluster center).
     */
    public void setVocabulary(Mat vocabulary) {
        dmatcher.clear();
        this.vocabulary = vocabulary;
//        System.out.println("Vocabulart was added "+vocabulary.rows());
        dmatcher.add(Arrays.asList(vocabulary));
    }

    /**
     * Returns the set vocabulary.
     *
     * @return vocabulary.
     */
    public Mat getVocabulary() {
        return vocabulary;
    }

    /**
     * Computes an image descriptor using the set visual vocabulary.
     *
     * @param image               Image, for which the descriptor is computed.
     * @param keypoints           Keypoints detected in the input image.
     * @param imgDescriptor       Computed output image descriptor.
     * @param pointIdxsOfClusters Indices of keypoints that belong to the cluster. This means that
     *                            pointIdxsOfClusters.get(i) are keypoint indices that belong to the i-th cluster (word of vocabulary)
     *                            returned if it is non-null.
     * @param descriptors         Descriptors of the image keypoints that are returned if they are non-null.
     */
    public void compute(Mat image, MatOfKeyPoint keypoints, Mat imgDescriptor,
                        List<List<Integer>> pointIdxsOfClusters, Mat descriptors) {
        imgDescriptor.release();
        if(keypoints.empty()) return;
        if(descriptors == null) descriptors = new Mat();
        dextractor.compute(image, keypoints, descriptors);
        compute(descriptors, imgDescriptor, pointIdxsOfClusters);
    }

    /**
     * @param keypointDescriptors Computed descriptors to match with vocabulary.
     * @param imgDescriptor       Computed output image descriptor.
     * @param pointIdxsOfClusters Indices of keypoints that belong to the cluster. This means that
     *                            pointIdxsOfClusters[i] are keypoint indices that belong to the i -th cluster (word of vocabulary)
     *                            returned if it is non-null.
     */
    public void compute(Mat keypointDescriptors, Mat imgDescriptor, List<List<Integer>> pointIdxsOfClusters) {
        if(vocabulary.empty()) throw new IllegalStateException("Vocabulary is empty.");

        MatOfDMatch matOfDMatch = new MatOfDMatch();
        try{
            dmatcher.match(keypointDescriptors, matOfDMatch);
        } catch (Exception e){
            System.out.println(dmatcher.getTrainDescriptors().get(0).rows()+" "+dmatcher.getTrainDescriptors().get(0).cols());
            System.out.println(keypointDescriptors.rows()+" x "+keypointDescriptors.cols());
           e.printStackTrace();
        }
        if (pointIdxsOfClusters != null) {

            pointIdxsOfClusters.clear();
        }
        imgDescriptor.create(1, descriptorsSize(), descriptorsType());
        imgDescriptor.setTo(Scalar.all(0));

        List<DMatch> matches = matOfDMatch.toList();
        for(int i=0; i<matches.size(); i++){
            int queryIdx = matches.get(i).queryIdx;
            int trainIdx = matches.get(i).trainIdx; // cluster idx
            if(queryIdx != i) throw new IllegalArgumentException("Illegal keypointsDescriptors index");
            float[] value = new float[1];
            imgDescriptor.get(0, trainIdx, value);
            value[0] += 1f;
            imgDescriptor.put(0, trainIdx, value);
            if(pointIdxsOfClusters != null) pointIdxsOfClusters.get(trainIdx).add(queryIdx);
        }
        // Normalize descriptor
        Core.divide(keypointDescriptors.size().height, imgDescriptor, imgDescriptor);
    }

    public void compute2(Mat image, List<KeyPoint> keypoints, Mat imgDescriptor) {
        compute(image, new MatOfKeyPoint(keypoints.toArray(new KeyPoint[keypoints.size()])), imgDescriptor, null, null);
    }

    /**
     * Returns an image descriptor size if the vocabulary is set. Otherwise, it returns 0.
     * @return image descriptor size or 0 if vocabulary is not set.
     */
    public int descriptorsSize() {
        return vocabulary.empty() ? 0 : vocabulary.rows();
    }

    /**
     * Returns an image descriptor type.
     * @return image descriptor type.
     */
    public int descriptorsType() {
        return CvType.CV_32FC1;
    }
}
