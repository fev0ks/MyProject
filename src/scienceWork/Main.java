package scienceWork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scienceWork.ObjectClasses.Picture;
import scienceWork.view.AlgorithmHistogramController;
import scienceWork.view.RootLayoutController;
import scienceWork.view.StartMenuController;
import scienceWork.view.ToolsController;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main  extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args){
      //  System.out.println(5/0);
        launch(args);
      //  ConnectorDB connectorDB= new ConnectorDB();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Диплом");
       // this.primaryStage.getIcons().add(new Image("resources/address_book.png"));
        initRootLayout();
        showToolsScene();

    }
    public void initRootLayout() {
        try {
          //  System.err.println("FXML resource: " + Main.class.getResource("view/rootBorder.fxml"));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/rootBorder.fxml"));

            rootLayout = (BorderPane) loader.load();
            rootLayout.setPrefSize(600,515);
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
           // System.err.println("FXML resource: " + Main.class.getResource("view/toolsScene.fxml"));
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/toolsScene.fxml"));
            AnchorPane startMenu = (AnchorPane) loader.load();

            rootLayout.setCenter(startMenu);

            // Даём контроллеру доступ к главному приложению.
            ToolsController controller = loader.getController();
            controller.setMainApp(this);
            File file=showChooseDir();
            if(file==null) System.exit(0);
            else
            controller.setDir(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File showChooseDir(){
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
           //System.out.println("FXML resource: " + Main.class.getResource("view/startMenu.fxml"));
            loader.setLocation(Main.class.getResource("view/startMenu.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage startMenuStage = new Stage();
            startMenuStage.setTitle("Choose directory");
            startMenuStage.initModality(Modality.WINDOW_MODAL);
            startMenuStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            startMenuStage.setScene(scene);

            // Передаём адресата в контроллер.
            StartMenuController controller = loader.getController();
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
            rootLayout.setPrefSize(800,615);
            // Передаёт адресатов в контроллер.
            AlgorithmHistogramController controller = loader.getController();
            controller.setAlgorithmData(pictureList,algName);

            dialogStage.show();
            rootLayout.setPrefSize(600,515);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
