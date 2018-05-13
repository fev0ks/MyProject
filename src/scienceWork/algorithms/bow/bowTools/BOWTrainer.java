package scienceWork.algorithms.bow.bowTools;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 08/05/16.
 */
public abstract class BOWTrainer {
    protected List<Mat> descriptors;
    protected int size;

    public BOWTrainer() {
        descriptors = new ArrayList<Mat>();
        size = 0;
    }

    public void add(Mat descriptors) {
        if(descriptors.empty())
            throw new IllegalArgumentException("Mat empty!");
        else if(!this.descriptors.isEmpty() &&
                ( this.descriptors.get(0).cols() != descriptors.cols() ||
                        this.descriptors.get(0).type() != descriptors.type() ))
                throw new IllegalArgumentException("cols or type not match to previous descriptors");

        this.descriptors.add(descriptors);
        size += descriptors.rows();
    }

    public List<Mat> getDescriptors() {
        return new ArrayList<Mat>(descriptors);
    }

    public int descriptorsCount() {
        return size;
    }

    public void clear() {
        descriptors.clear();
        size = 0;
    }

    public abstract Mat cluster();
    public abstract Mat cluster(Mat descriptors);
}
