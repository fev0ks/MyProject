package scienceWork.objects.picTypesData;

import org.opencv.core.Mat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ImgTypesClusters {

    public static Map<String, Mat> trainedClusters = new HashMap<>();

    public static List getClustersTypes(){
        List<String> types = new LinkedList();
        for(Map.Entry<String, Mat> entry: trainedClusters.entrySet()){
            types.add(entry.getKey());
        }
        return types;
    }
}
