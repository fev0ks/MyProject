package scienceWork.objects.constants;


import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

public class Settings {
    private static int countWords = 600;


    private static int countBOWClusters = 10;
    private static int methodKP = FeatureDetector.ORB;
    private static int methodDescr = DescriptorExtractor.ORB;
    private static int methodMatcher = DescriptorMatcher.FLANNBASED;
    private static int scaleImageRatio = 800;
    private static boolean saveBOW = true;
    private static boolean saveClassifier = false;

    public static final boolean SAVE_DATA = true;

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

    public static boolean isSaveBOW() {
        return saveBOW;
    }

    public static void setSaveBOW(boolean saveBOW) {
        Settings.saveBOW = saveBOW;
    }

    public static boolean isSaveClassifier() {
        return saveClassifier;
    }

    public static void setSaveClassifier(boolean saveClassifier) {
        Settings.saveClassifier = saveClassifier;
    }


    private void Settings() {

    }

    public void resetSettingsToDefault() {
        countWords = 500;
        methodKP = FeatureDetector.ORB;
        methodDescr = DescriptorExtractor.ORB;
        method = Constants.ORB;
    }

    public static boolean isSaveData() {
        return SAVE_DATA;
    }

    public static int getScaleImageRatio() {
        return scaleImageRatio;
    }

    public static void setScaleImageRatio(int scaleImageRatio) {
        Settings.scaleImageRatio = scaleImageRatio;
    }

    public static int getCountWords() {
        return countWords;
    }

    public static void setCountOfClusters(int countClusters) {
        Settings.countWords = countClusters;
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
        return countWords;
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
                "; Count of words: "+ countWords +
                 "; }";
    }
}
