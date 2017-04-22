package scienceWork.algorithms;

import scienceWork.Interfaces.IImageWorker;
import scienceWork.ObjectClasses.Algorithms;
import scienceWork.dataBase.ManagerDB;

/*
    управление доступными алгоритмами обработки изображения
 */

//TODO
public class ImageWorker implements IImageWorker {

    public static volatile ImageWorker instance;
    private final ManagerDB managerDB=ManagerDB.getInstance();
    private ImageWorker() {

    }

    public static ImageWorker getInstance() {
        if (instance == null)
            synchronized (Algorithms.class) {
                if (instance == null)
                    instance = new ImageWorker();
            }
        return instance;
    }

//    public ObservableList<Picture> setPicDimensions(ObservableList<Picture> pictureList) {
//        WorkFolder workFolder = WorkFolder.getInstance();
//        for (Picture picture : pictureList) {
//            picture.setDimension(workFolder.getPicDimensions(picture.getPicFile()));//выкинуть в алгоритмы!!! реализовать по потокам
//            managerDB.setPicDimensionsDB(picture);
//        }
//        return pictureList;
//    }
    public void run(){

    }
}
