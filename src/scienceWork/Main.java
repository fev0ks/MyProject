package scienceWork;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import scienceWork.FxWorker.FxHelper;
import scienceWork.objects.Picture;
import scienceWork.view.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main extends Application {
    private Stage primaryStage;
    private static boolean firstSelect = true;
    private BorderPane rootLayout;

    public Main() {
    }

    public static void main(String[] args) {
        //  System.out.println(5/0);
        launch(args);
        //  ConnectorDB connectorDB= new ConnectorDB();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Диплом");
//        Platform.setImplicitExit(false);

        // this.primaryStage.getIcons().add(new Image("resources/address_book.png"));
        initRootLayout();
        showToolsScene();

    }

    private void setCSSforScene(Scene scene){
        scene.getStylesheets().add(Main.class.getResource("resources/bootstrap3.css").toExternalForm());
    }

    private void setCSSforPane(Pane pane){
        pane.getStylesheets().add(Main.class.getResource("resources/bootstrap3.css").toExternalForm());
    }

    private void initRootLayout() {
        int HEIGHT_MENU = 25;
        int WIDTH_ROOT_LAYOUT = 680;
        int HEIGHT_ROOT_LAYOUT  = 655;

        try {
            //  System.err.println("FXML resource: " + Main.class.getResource("view/rootBorder.fxml"));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/rootBorder.fxml"));

            rootLayout = loader.load();

            rootLayout.setPrefSize(WIDTH_ROOT_LAYOUT, HEIGHT_ROOT_LAYOUT + HEIGHT_MENU);
            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest((WindowEvent event1) -> System.exit(0));

            // Даём контроллеру доступ к главному прилодению.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPage();
        }
    }

    public void showToolsScene() {
        try {
            // System.err.println("FXML resource: " + Main.class.getResource("view/mainView.fxml"));
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/mainView.fxml"));
            AnchorPane page =  loader.load();

            rootLayout.setCenter(page);
//            Scene scene = new Scene(page);
            setCSSforPane(rootLayout);

//            primaryStage.setScene(scene);
            // Даём контроллеру доступ к главному приложению.
            MainController controller = loader.getController();
            controller.setMainApp(this);
//            controller.setDialogStage(primaryStage);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
//            startMenuStage.showAndWait();

            File file = showChooseDir();
            System.out.println("showToolsScene " + file + " " + firstSelect);
            if (file != null) {
                controller.setDir(file);
                firstSelect = false;
            }
            if (file == null && firstSelect) {
                System.exit(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File showChooseDir() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            //System.out.println("FXML resource: " + Main.class.getResource("view/choseDIrectoryView.fxml"));
            loader.setLocation(Main.class.getResource("view/choseDIrectoryView.fxml"));
            AnchorPane page = loader.load();

            // Создаём диалоговое окно Stage.
            Stage startMenuStage = new Stage();
            startMenuStage.setTitle("Choose directory");
            startMenuStage.initModality(Modality.WINDOW_MODAL);
            startMenuStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            setCSSforScene(scene);
            startMenuStage.setScene(scene);

            // Передаём адресата в контроллер.
            ChoseDirectoryController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(startMenuStage);


            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            startMenuStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPage();
            return null;
        }
    }

    public void showResultPage(List<List<Picture>> pictureLists) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/resultsView.fxml"));
            AnchorPane page = loader.load();

            // Создаём диалоговое окно Stage.
            Stage startMenuStage = new Stage();
            startMenuStage.setTitle("Check results");
            startMenuStage.initModality(Modality.WINDOW_MODAL);
            startMenuStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            setCSSforScene(scene);
            startMenuStage.setScene(scene);

            // Передаём адресата в контроллер.
            ResultController controller = loader.getController();
            controller.setDialogStage(startMenuStage);
            controller.setMainApp(this);
            controller.initDataPage(pictureLists);

            startMenuStage.heightProperty().addListener((obs, oldVal, newVal) -> controller.resizeImageView());
            startMenuStage.widthProperty().addListener((obs, oldVal, newVal) -> controller.resizeImageView());


            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            startMenuStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPage();
        }
    }

    public void showSettingsMenu() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/optionsView.fxml"));
            AnchorPane page = loader.load();

            // Создаём диалоговое окно Stage.
            Stage startMenuStage = new Stage();
            startMenuStage.setTitle("Settings");
            startMenuStage.initModality(Modality.WINDOW_MODAL);
            startMenuStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            setCSSforScene(scene);
            startMenuStage.setScene(scene);

            // Передаём адресата в контроллер.
            OptionsController controller = loader.getController();
            controller.setDialogStage(startMenuStage);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            startMenuStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorPage();
        }
    }

    public void showVocabularyMenu() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/vocabularyView.fxml"));
            AnchorPane page = loader.load();

            // Создаём диалоговое окно Stage.
            Stage startMenuStage = new Stage();
            startMenuStage.setTitle("Vocabulary");
            startMenuStage.initModality(Modality.WINDOW_MODAL);
            startMenuStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            setCSSforScene(scene);
            startMenuStage.setScene(scene);

            // Передаём адресата в контроллер.
            VocabularyController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(startMenuStage);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            startMenuStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPage();
        }
    }

    public void showInfoAboutPicture(Picture picture) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/pictureView.fxml"));
            AnchorPane page =  loader.load();

            // Создаём диалоговое окно Stage.
            Stage startMenuStage = new Stage();
            startMenuStage.setTitle("Picture " + picture.getName());
            startMenuStage.initModality(Modality.WINDOW_MODAL);
            startMenuStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            setCSSforScene(scene);
            startMenuStage.setScene(scene);

            // Передаём адресата в контроллер.
            PictureController controller = loader.getController();
            controller.setPicture(picture);
            controller.setMainApp(this);
            controller.initFirstWindow();
            controller.setDialogStage(startMenuStage);

            startMenuStage.heightProperty().addListener((obs, oldVal, newVal) -> controller.resizeWindowListener());
            startMenuStage.widthProperty().addListener((obs, oldVal, newVal) -> controller.resizeWindowListener());

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            startMenuStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPage();
        }
    }

    public void showAlgorithmStatistics(Picture picture) {
        try {
            // Загружает fxml-файл и создаёт новую сцену для всплывающего окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/histogram.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Data");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            setCSSforScene(scene);
            dialogStage.setScene(scene);
//            rootLayout.setPrefSize(800, 615);
            // Передаёт адресатов в контроллер.
            AlgorithmHistogramController controller = loader.getController();
            controller.setAlgorithmData(picture);
            controller.setMainApp(this);

            dialogStage.show();
//            rootLayout.setPrefSize(600, 515);

        } catch (IOException e) {
            e.printStackTrace();
            showErrorPage();
        }
    }

//    public boolean getStatus() {
//        return firstSelect;
//    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void showErrorPage(){
        FxHelper.showMessage("Error", "Failed load a page", "Please check your code", Alert.AlertType.ERROR, this);
    }
}
