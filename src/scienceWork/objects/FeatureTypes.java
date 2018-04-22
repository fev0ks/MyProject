package scienceWork.objects;

import java.util.HashMap;
import java.util.Map;

public enum FeatureTypes {
    ORB(1, 5, "ORB"),
    AKAZE(2, 12, "AKAZE"),
    BRISK(3, 11, "BRISK");

    private int featureID;
    private String feature;
    private int openCvKP;

    FeatureTypes(int featureID, int openCvKP, String feature)
    {
        this.featureID = featureID;
        this.feature = feature;
        this.openCvKP = openCvKP;
    }

    private static final Map<Integer, Integer> idToEnum = new HashMap<>();

    static
    {
        for (FeatureTypes feature : values())
        {
            idToEnum.put(feature.getOpenCvKP(), feature.featureID);
        }
    }


    public static int valueOf(int openCvKP){
        return idToEnum.get(openCvKP);
    }

    public int getFeatureID() {
        return featureID;
    }

    public int getOpenCvKP() {
        return openCvKP;
    }

    @Override
    public String toString()
    {
        return this.feature + " ("+this.featureID+")";
    }
}
