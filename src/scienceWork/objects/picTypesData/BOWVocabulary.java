package scienceWork.objects.picTypesData;

import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import scienceWork.algorithms.bow.bowTools.BOWImgDescriptorExtractor;
import scienceWork.objects.constants.Settings;

import java.util.*;

/**
 * Created by mixa1 on 25.02.2018.
 */
public class BOWVocabulary {
    public static Map<String, Mat> vocabularies = new HashMap<>();
    public static Mat commonVocabulary = new Mat();
    public static List<String> classesNumbers = new ArrayList<>();
    public static List getClustersTypes(){
        List<String> types = new LinkedList();
        for(Map.Entry<String, Mat> entry: vocabularies.entrySet()){
            types.add(entry.getKey());
        }
        return types;
    }
    public static BOWImgDescriptorExtractor getBOWImgDescriptorExtractor() {
        DescriptorExtractor descriptor = DescriptorExtractor.create(Settings.getMethodDescr());
        DescriptorMatcher matcher = DescriptorMatcher.create(Settings.getMethodMatcher());
        return new BOWImgDescriptorExtractor(descriptor, matcher);
    }
}