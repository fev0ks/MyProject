package scienceWork.FxWorker;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import scienceWork.Main;
import scienceWork.objects.Picture;

import java.awt.image.BufferedImage;
import java.util.List;

public class FxHelper {

    public static <T> ObservableList<T> convertListsToObservableList(List<List<T>> lists) {
        ObservableList<T> observableList = FXCollections.observableArrayList();
        for (List<T> list : lists) {
            observableList.addAll(list);
        }
        return observableList;
    }

    public static <T> ObservableList<T> convertListToObservableList(List<T> list) {
        ObservableList<T> observableList = FXCollections.observableArrayList();
        observableList.addAll(list);
        return observableList;
    }


    public static void showMessage(String title, String message, String contentText, Alert.AlertType alertType, Main mainApp) {
        Alert alert = new Alert(alertType);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static Image convertAwtImageToFxImage(BufferedImage bufferedImage) {
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    public static IntegerProperty convertIntegerToSimpleIntegerProperty(int i) {
        return new SimpleIntegerProperty(i);
    }

    public static StringProperty convertStringToStringProperty(String str) {
        return new SimpleStringProperty(str);
    }

    public static DoubleProperty convertDoubleToDoubleProperty(double d) {
        return new SimpleDoubleProperty(d);
    }

}
