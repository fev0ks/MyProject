package scienceWork.objects.constants;

import java.util.HashMap;
import java.util.Map;

public enum Classifiers {
    SVM(1, "SVM"),
    LR(2, "LR"),
    ASSIGNED(3, "Assigned");

    private int listValueID;
    private String name;

    Classifiers(int lvID, String lv)
    {
        listValueID = lvID;
        name = lv;
    }

    public int getListValueID() {
        return listValueID;
    }

    private static final Map<Integer, Classifiers> idToEnum = new HashMap<>();

    static
    {
        for (Classifiers algorithm : values())
        {
            idToEnum.put(algorithm.getListValueID(), algorithm);
        }
    }

    public static Classifiers valueOf(int lvid)
    {
        return idToEnum.get(lvid);
    }


    @Override
    public String toString()
    {
        return this.name + " ("+this.listValueID+")";
    }
}
