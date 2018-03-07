package scienceWork.objects.picTypesData;

import org.opencv.core.Mat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by mixa1 on 25.02.2018.
 */
public class BOWVocabulary {
    public static Map<String, Mat> vocabularies = new HashMap<>();

    public static List getClustersTypes(){
        List<String> types = new LinkedList();
        for(Map.Entry<String, Mat> entry: vocabularies.entrySet()){
            types.add(entry.getKey());
        }
        return types;
    }
}
