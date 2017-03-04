package scienceWork.ObjectClasses;

import javafx.collections.ObservableList;
import scienceWork.WorkFolder;
import scienceWork.dataBase.ManagerDB;

public class Algorithms {
    public static volatile Algorithms instance;
    private final ManagerDB managerDB=ManagerDB.getInstance();
    private Algorithms() {

    }

    public static Algorithms getInstance() {
        if (instance == null)
            synchronized (Algorithms.class) {
                if (instance == null)
                    instance = new Algorithms();
            }
        return instance;
    }

    public ObservableList<Picture> setPicDimensions(ObservableList<Picture> pictureList) {
        WorkFolder workFolder = WorkFolder.getInstance();
        for (Picture picture : pictureList) {
            picture.setDimension(workFolder.getPicDimensions(picture.getPicFile()));//выкинуть в алгоритмы!!! реализовать по потокам
            managerDB.setPicDimensionsDB(picture);
        }
        return pictureList;
    }
}
