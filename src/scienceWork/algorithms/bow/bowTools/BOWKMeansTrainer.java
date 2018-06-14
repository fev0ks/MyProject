package scienceWork.algorithms.bow.bowTools;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;


public class BOWKMeansTrainer extends BOWTrainer {
    private int clusterCount;
    private TermCriteria termCriteria;
    private int attempts;
    private int flags;

    public BOWKMeansTrainer(int clusterCount, TermCriteria termCriteria, int attempts, int flags) {
        this.clusterCount = clusterCount;
        this.termCriteria = termCriteria;
        this.attempts = attempts;
        this.flags = flags;
    }

    public BOWKMeansTrainer(int clusterCount, TermCriteria termCriteria, int attempts) {
        this(clusterCount, termCriteria, attempts, Core.KMEANS_PP_CENTERS);
    }

    public BOWKMeansTrainer(int clusterCount, TermCriteria termCriteria) {
        this(clusterCount, termCriteria, 3);
    }

    public BOWKMeansTrainer(int clusterCount) {
        this(clusterCount, new TermCriteria());
    }

    @Override
    public Mat cluster() {
        if (descriptors.isEmpty())
            throw new IllegalStateException("Empty descriptors. No descriptors to cluster.");
        Mat mergedDescriptors = new Mat(descriptorsCount(), descriptors.get(0).cols(), descriptors.get(0).type());
        for (int i = 0, start = 0; i < descriptors.size(); i++) {
            Mat submut = mergedDescriptors.rowRange(start, start + descriptors.get(i).rows());
            descriptors.get(i).copyTo(submut);
            start += descriptors.get(i).rows();
        }
        return cluster(mergedDescriptors);
    }

    @Override
    public Mat cluster(Mat descriptors) {
        Mat labels = new Mat(), vocabulary = new Mat();
        Core.kmeans(descriptors, clusterCount, labels, termCriteria, attempts, flags, vocabulary);
        return vocabulary;
    }
}
