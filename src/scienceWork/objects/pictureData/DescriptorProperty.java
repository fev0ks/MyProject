package scienceWork.objects.pictureData;


import javafx.beans.property.SimpleIntegerProperty;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

public class DescriptorProperty {
    private MatOfKeyPoint matOfKeyPoint;
    private Mat matOfDescription;
    private Mat centersOfDescriptors;
    private int countOfDescr;


    public int getCountOfDescr() {
        return matOfDescription == null ? 0 : matOfDescription.height();
    }

    public void setCountOfDescr(int countOfDescr) {
        this.countOfDescr = countOfDescr;
    }

    public SimpleIntegerProperty getCountOfDescrProperty() {
        return new SimpleIntegerProperty(this.countOfDescr);
    }

    public DescriptorProperty(MatOfKeyPoint matOfKeyPoint, Mat matOfDescription, Mat centersOfDescriptors) {
        this.matOfKeyPoint = matOfKeyPoint;
        this.matOfDescription = matOfDescription;
        this.centersOfDescriptors = centersOfDescriptors;
        this.countOfDescr = matOfDescription.height();
    }

    public DescriptorProperty(MatOfKeyPoint matOfKeyPoint, Mat matOfDescription) {
        this.matOfKeyPoint = matOfKeyPoint;
        this.matOfDescription = matOfDescription;
    }

    public DescriptorProperty() {
        this.countOfDescr = 0;
    }

    public Mat getMatOfDescription() {
        return matOfDescription;
    }

    public MatOfKeyPoint getMatOfKeyPoint() {
        return matOfKeyPoint;
    }

    public Mat getCentersOfDescriptors() {
        return centersOfDescriptors;
    }

    public void setCentersOfDescriptors(Mat centersOfDescriptors) {
        this.centersOfDescriptors = centersOfDescriptors;
    }

    //    public void setMatOfKeyPoint(MatOfKeyPoint matOfKeyPoint) {
//        this.matOfKeyPoint = matOfKeyPoint;
//    }
}
