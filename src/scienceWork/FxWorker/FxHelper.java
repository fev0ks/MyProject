package scienceWork.FxWorker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import scienceWork.Main;
import scienceWork.objects.Picture;

import java.util.List;

public class FxHelper {

    public static ObservableList<Picture> convertListsToObservableList(List<List<Picture>> pictLists) {
        ObservableList<Picture> observableListPicures = FXCollections.observableArrayList();
        for (List<Picture> pictList : pictLists) {
            observableListPicures.addAll(pictList);
        }
        return observableListPicures;
    }

    public static void showMessage(String title, String message, String contentText, Alert.AlertType alertType, Main mainApp) {
        Alert alert = new Alert(alertType);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
