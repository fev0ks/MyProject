package scienceWork.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import scienceWork.Main;

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
}
