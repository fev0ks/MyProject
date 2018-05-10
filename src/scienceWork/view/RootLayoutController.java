package scienceWork.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import scienceWork.FxWorker.FxHelper;
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
        FxHelper.showMessage(
                "Diploma app",
                " ",
                "Author: Mikhail Patin\n:)",
                Alert.AlertType.INFORMATION,
                mainApp);
    }

}
