package scienceWork.objects;

import java.util.HashMap;
import java.util.Map;

/* some shit */
public enum FeatureTypes {
    ORB(1, 5, "ORB"),
    AKAZE(2, 12, "AKAZE"),
    BRISK(3, 11, "BRISK");

    private int featureID;
    private String label;
    private int openCvKP;

    FeatureTypes(int featureID, int openCvKP, String label)
    {
        this.featureID = featureID;
        this.label = label;
        this.openCvKP = openCvKP;
    }

    private static final Map<Integer, Integer> cvTypeToFeatureId = new HashMap<>();
    private static final Map<Integer, String> cvTypeToString = new HashMap<>();
    private static final Map<Integer, String> featureIdToString = new HashMap<>();

    static
    {
        for (FeatureTypes feature : values())
        {
            cvTypeToFeatureId.put(feature.getOpenCvKP(), feature.featureID);
        }

        for (FeatureTypes feature : values())
        {
            cvTypeToString.put(feature.getOpenCvKP(), feature.label);
        }
        for (FeatureTypes feature : values())
        {
            featureIdToString.put(feature.getFeatureID(), feature.label);
        }
    }

    public static String getLabelById(int featureID){
        return featureIdToString.get(featureID);
    }

    public int getFeatureID() {
        return featureID;
    }

    public static int getFeatureId(int openCvKP){
        return cvTypeToFeatureId.get(openCvKP);
    }


    public static String getLabel(int openCvKP){
        return cvTypeToString.get(openCvKP);
    }

    public int getOpenCvKP() {
        return openCvKP;
    }

    @Override
    public String toString()
    {
        return this.label + " ("+this.featureID+")";
    }
}
