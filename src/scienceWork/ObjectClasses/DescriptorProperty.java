package scienceWork.ObjectClasses;


import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

public class DescriptorProperty {
    private MatOfKeyPoint matOfKeyPoint;
    private Mat mat;

    public DescriptorProperty(MatOfKeyPoint matOfKeyPoint, Mat mat) {
        this.matOfKeyPoint = matOfKeyPoint;
        this.mat=mat;
    }

    public MatOfKeyPoint getMatOfKeyPoint() {
        return matOfKeyPoint;
    }

    public void setMatOfKeyPoint(MatOfKeyPoint matOfKeyPoint) {
        this.matOfKeyPoint = matOfKeyPoint;
    }
}
