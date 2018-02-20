package scienceWork.objects;

import org.opencv.core.Mat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Clusters {

    public static Map<String, Mat> addGeneralizedClustersForInputTypeImage = new HashMap<>();

    public static List getClustersTypes(){
        List<String> types = new LinkedList();
        for(Map.Entry<String, Mat> entry: addGeneralizedClustersForInputTypeImage.entrySet()){
            types.add(entry.getKey());
        }
        return types;
    }
}
