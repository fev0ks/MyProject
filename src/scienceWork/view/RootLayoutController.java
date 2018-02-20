package scienceWork.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import scienceWork.Main;

import java.io.File;

/**
 * Created by mixa1 on 18.12.2016.
 */
public class RootLayoutController {
    private Main mainApp;

    /**
     * Вызывается главным приложением, чтобы оставить ссылку на самого себя.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DiplomApp");
        alert.setHeaderText("About");
        alert.setContentText("Author: Mikhail Patin\n:)");

        alert.showAndWait();
    }

    @FXML
    private void chouseNewDirectory(){
//        new Main().showToolsScene();
//    }
        MainController controller = new MainController();
        File file = new Main().showChooseDir();
        if (file == null) System.exit(0);
        else {
            Platform.runLater(() -> {
                controller.setDir(file);
                controller.clearTable();

            });
        }
//        MainController controller = new MainController();
////        MainController controller = loader.getController();
////        controller.setMainApp(this);
//        File file = new Main().showChooseDir();
//        if (file == null) System.exit(0);
//        else
//            controller.setDir(file);
    }
}
