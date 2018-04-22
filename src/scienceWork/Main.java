package scienceWork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scienceWork.objects.Picture;
import scienceWork.view.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main extends Application {
    private Stage primaryStage;
    private static boolean firstSelect = true;
    private BorderPane rootLayout;
    private final int HEIGHT_MENU = 25;
    private final int WIDTH_ROOT_LAYOUT = 680;
    private final int HEIGHT_ROOT_LAYOUT = 655;

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

    public void initRootLayout() {
        try {
            //  System.err.println("FXML resource: " + Main.class.getResource("view/rootBorder.fxml"));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/rootBorder.fxml"));

            rootLayout = (BorderPane) loader.load();
            rootLayout.setPrefSize(WIDTH_ROOT_LAYOUT, HEIGHT_ROOT_LAYOUT + HEIGHT_MENU);
            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Даём контроллеру доступ к главному прилодению.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showToolsScene() {
        try {
            // System.err.println("FXML resource: " + Main.class.getResource("view/mainView.fxml"));
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/mainView.fxml"));
            AnchorPane startMenu = (AnchorPane) loader.load();

            rootLayout.setCenter(startMenu);

            // Даём контроллеру доступ к главному приложению.
            MainView controller = loader.getController();
            controller.setMainApp(this);
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
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage startMenuStage = new Stage();
            startMenuStage.setTitle("Choose directory");
            startMenuStage.initModality(Modality.WINDOW_MODAL);
            startMenuStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
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
            return null;
        }
    }

    public String setNameOfTypeImages() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/selectImagesTypeView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage startMenuStage = new Stage();
            startMenuStage.setTitle("Choose name of type images");
            startMenuStage.initModality(Modality.WINDOW_MODAL);
            startMenuStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            startMenuStage.setScene(scene);

            // Передаём адресата в контроллер.
            SelectImagesTypeController controller = loader.getController();
            controller.setDialogStage(startMenuStage);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            startMenuStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showSettingsMenu() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/optionsView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage startMenuStage = new Stage();
            startMenuStage.setTitle("Settings");
            startMenuStage.initModality(Modality.WINDOW_MODAL);
            startMenuStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            startMenuStage.setScene(scene);

            // Передаём адресата в контроллер.
            OptionsController controller = loader.getController();
            controller.setDialogStage(startMenuStage);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            startMenuStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showInfoAboutPicture(Picture picture) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/pictureView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage startMenuStage = new Stage();
            startMenuStage.setTitle("Picture " + picture.getName());
            startMenuStage.initModality(Modality.WINDOW_MODAL);
            startMenuStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            startMenuStage.setScene(scene);

            // Передаём адресата в контроллер.
            PictureController controller = loader.getController();
            controller.setPicture(picture);
            controller.initFirstWindow();
            controller.setDialogStage(startMenuStage);
            System.out.println(startMenuStage.getMinHeight());
            System.out.println(startMenuStage.getMinWidth());
//            startMenuStage.fullScreenProperty().addListener((obs, oldVal, newVal) -> {
//                controller.resizeWindowListener();
//            });
            startMenuStage.heightProperty().addListener((obs, oldVal, newVal) -> {
                controller.resizeWindowListener();
            });
            startMenuStage.widthProperty().addListener((obs, oldVal, newVal) -> {
                controller.resizeWindowListener();
            });
//            startMenuStage.heightProperty().addListener((obs, oldVal, newVal) -> {
//                controller.resizeWindowListener();
//            });
//            startMenuStage.widthProperty().addListener((obs, oldVal, newVal) -> {
//                controller.resizeWindowListener();
//            });
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            startMenuStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAlgorithmStatistics(List<Picture> pictureList, String algName) {
        try {
            // Загружает fxml-файл и создаёт новую сцену для всплывающего окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/histogram.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Data");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            rootLayout.setPrefSize(800, 615);
            // Передаёт адресатов в контроллер.
            AlgorithmHistogramController controller = loader.getController();
            controller.setAlgorithmData(pictureList, algName);

            dialogStage.show();
            rootLayout.setPrefSize(600, 515);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getStatus() {
        return firstSelect;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
