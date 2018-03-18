package scienceWork.objects.constants;


import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

public class Settings {
    private static int countThreads = 4;
    private static int countClusters = 10;
    private static int countBOWClusters = 10;
    private static int methodKP = FeatureDetector.ORB;
    private static int methodDescr = DescriptorExtractor.ORB;
    private static int methodMatcher = DescriptorMatcher.FLANNBASED;

    private static String method = Constants.ORB;
    private static Settings settings;

    public static Settings getInstance() {
        if (settings == null) {
            settings = new Settings();
            return settings;
        } else {
            return settings;
        }
    }

    private void Settings() {

    }

    public void resetSettingsToDefault() {
        countThreads = 4;
        countClusters = 10;
        methodKP = FeatureDetector.ORB;
        methodDescr = DescriptorExtractor.ORB;
        method = Constants.ORB;
    }

    public static int getCountThreads() {
        return countThreads;
    }

    public static void setCountOfThreads(int countThreads) {
        Settings.countThreads = countThreads;
    }

    public static int getCountClusters() {
        return countClusters;
    }

    public static void setCountOfClusters(int countClusters) {
        Settings.countClusters = countClusters;
    }

    public static int getMethodKP() {
        return methodKP;
    }

    public static void setMethodKP(int methodKP) {
        Settings.methodKP = methodKP;
    }

    public static int getMethodDescr() {
        return methodDescr;
    }

    public static void setMethodDescr(int methodDescr) {
        Settings.methodDescr = methodDescr;
    }

    public static String getMethod() {
        return method;
    }

    public static void setMethod(String method) {
        Settings.method = method;
    }


    public static int getCountBOWClusters() {
        return countClusters;
    }

    public static void setCountBOWClusters(int countBOWClusters) {
        Settings.countBOWClusters = countBOWClusters;
    }


    public static int getMethodMatcher() {
        return methodMatcher;
    }

    public static void setMethodMatcher(int methodMatcher) {
        Settings.methodMatcher = methodMatcher;
    }

    @Override
    public String toString() {
        return "Settings{ " +
                "Method search KP/Desc: "+ method +
                "; Count of threads: "+ countThreads +
                "; Count of clusters: "+ countClusters +
                 "; }";
    }
}
