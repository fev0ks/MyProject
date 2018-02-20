package scienceWork.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import scienceWork.FileWorker;
import scienceWork.Main;
import scienceWork.algorithms.ClusteringThroughDescriptionClusters;
import scienceWork.objects.Clusters;
import scienceWork.objects.Directory;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;

import java.io.File;
import java.util.*;

public class MainController {
    private Main mainApp;
    private Directory directory;
    private String typeImages;
    private final FileWorker fileWorker = FileWorker.getInstance();
//    private final ManagerDB managerDB = ManagerDB.getInstance();

    @FXML
    private TextArea infoTA;
    @FXML
    private TableView<Picture> picTable;
    @FXML
    private TableColumn<Picture, Integer> countColumn;
    @FXML
    private TableColumn<Picture, String> namePicColumn;
    @FXML
    private TableColumn<Picture, Double> sizePicColumn;
    @FXML
    private TableColumn<Picture, String> dimensionsColumn;
    @FXML
    private TableColumn<Picture, String> groupColumn;
    @FXML
    private TableColumn<Picture, Integer> countOfDescriptorsColumn;
    @FXML
    private CheckBox dimensionsChB;
    @FXML
    private CheckBox distanceChB;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button groupingBT;
    @FXML
    private Button analysisBT;
    @FXML
    private Button newDirBT;
    @FXML
    private Button showHistogramBT;
    @FXML
    private Button resetDataBT;
    @FXML
    private ListView<Integer> countThreadLV;
    @FXML
    private ListView<String> typeMethodKeyPandDescrLV;
    @FXML
    private ListView<Integer> countClustersLV;
    @FXML
    private Label settingsLbl;

    private List<List<Picture>> pictList;
    private String message = "";

    public void setDir(File dir) {
        directory = new Directory();
        directory.setDirFile(dir);
        directory.setDir(dir.getAbsolutePath());
        directory.setCountFiles(dir.listFiles().length);
//        message = managerDB.dirToDB(dir);
        fileWorker.setProgressBar(progressBar);
        infoTA.setText("Loading pictures from "+directory.getDir());
        Thread workFolderThread = new Thread(() -> {
            pictList = fileWorker.loadPicFromDir(dir);

        // System.out.println(" files: " +dir.listFiles().length+" pic: "+ pictList.size());
        infoTA.setText(directory.getDir() + " files: " + dir.listFiles().length + " pic: " + pictList.size());
        updateTable();
        });
        workFolderThread.start();
    }

    public Directory getDir() {
        return directory;
    }

    @FXML
    private void resetData() {
        infoTA.setText("Clearing " + Clusters.addGeneralizedClustersForInputTypeImage.size() + " type(s) of pictures");
        Clusters.addGeneralizedClustersForInputTypeImage = new HashMap<>();
    }

    @FXML
    private void choseNewDirectory() {
//        new Main().showToolsScene();
//    }
//        MainController controller = new MainController();
//        MainController controller = loader.getController();
//        controller.setMainApp(this);
        Main main = new Main();
        File file = main.showChooseDir();
        if (file != null) {
            clearTable();
            setDir(file);
        }
        if (file == null && main.getStatus()) {
            System.exit(0);
        }
    }

    //вывожу данные об изображениях в табилцу
    @FXML
    private void updateTable() {
        System.out.println(" files: " + directory.getDirFile().listFiles().length + " pic: " + pictList.size());
        if (pictList.size() > 0) {
            picTable.setItems(convertListsToObservableList(pictList));
            groupColumn.setCellValueFactory(cellData -> cellData.getValue().getGroupProperty());
            namePicColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            sizePicColumn.setCellValueFactory(cellData -> cellData.getValue().getSizeProperty().asObject());
            countColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
            dimensionsColumn.setCellValueFactory(cellData -> cellData.getValue().getDimensionsProperty().toPropertyString());
            countOfDescriptorsColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptorProperty().getCountOfDescrProperty().asObject());
//            System.out.println(pictList.get(0).getDescriptorProperty().getCountOfDescr() + " descr");
            dimensionsColumn.setCellValueFactory(cellData -> cellData.getValue().getDimensionsProperty().toPropertyString());
        } else {
            showMessage("NO IMAGES!!!");
            mainApp.showToolsScene();
        }
//        showMessage(message);
        loadPicToDB();
    }

    private ObservableList<Picture> convertListsToObservableList(List<List<Picture>> pictLists) {
        ObservableList<Picture> observableListPicures = FXCollections.observableArrayList();
        for (List<Picture> pictList : pictLists) {
            for (Picture picture : pictList) {
                observableListPicures.add(picture);
            }
        }
        return observableListPicures;
    }

    //загружаю изображения в базу данных
    private void loadPicToDB() {
//        message += (managerDB.picToDBfromDir(pictList));
    }


    @FXML
    private void showSettingsMenu() {
        new Main().showSettingsMenu();
        infoTA.setText(Settings.getInstance().toString() + "\n" + infoTA.getText());
    }

    private void setDisabledButtons(boolean disable) {
        groupingBT.setDisable(disable);
        analysisBT.setDisable(disable);
        newDirBT.setDisable(disable);
        showHistogramBT.setDisable(disable);
        resetDataBT.setDisable(disable);
    }

    /*
    Группировка изображений
     */
    @FXML
    private void groupingImagesToClasses() {

        setDisabledButtons(true);
        Thread mainThread = new Thread(() -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            new ClusteringThroughDescriptionClusters(progressBar).getImagesType(pictList.get(0));
//            clearTable();
//            updateTable();
            stopWatch.stop();
            viewWorkTime(stopWatch.getTime());

            clearTable();
            updateTable();
            setDisabledButtons(false);
        });
        mainThread.start();
    }


    @FXML
    private void analysis() {

        typeImages = new Main().setNameOfTypeImages();
        if (StringUtils.isBlank(typeImages)) {
            infoTA.setText("Select type of analysing images\n" + infoTA.getText());
        } else {
            infoTA.setText("Analysing type: " + typeImages + "\n" + infoTA.getText());
            setDisabledButtons(true);

            Thread mainThread = new Thread(() -> {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                new ClusteringThroughDescriptionClusters(typeImages, progressBar).findFeaturesForImages(pictList.get(0));
                stopWatch.stop();
                viewWorkTime(stopWatch.getTime());

                clearTable();
                updateTable();
                setDisabledButtons(false);
            });
            mainThread.start();

//          обращение к вьюхе из любого участка кода
//        Platform.runLater(() -> {
//            try {
//                Stage st = new Stage();
//                Parent sceneMain = FXMLLoader.load(getClass().getResource("/com/load/free/form/LoadFile.fxml"));
//                Scene scene = new Scene(sceneMain);
//                st.setScene(scene);
//                st.setMaximized(true);
//                st.setTitle("load");
//                st.show();
//            } catch (IOException ex) {
//                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });
        }
    }

    private void viewWorkTime(long finishTime) {
        System.out.println("Время: " + finishTime / 1000 + " сек, " + finishTime % 1000 + " мс;");
        message = "Завершено за " + finishTime / 1000 + " сек, " + finishTime % 1000 + " мс;";
        infoTA.setText(message + " " + Settings.getInstance().toString() + "\n" + infoTA.getText());
    }

    @FXML
    private void selectRowInTable() {

        Picture pictureFromTable = picTable.getSelectionModel().getSelectedItem();
        if (pictureFromTable != null) {
//            System.out.println(pictureFromTable.getName());
            picTable.getSelectionModel().clearSelection();
            new Main().showInfoAboutPicture(pictureFromTable);
//            infoTA.setText(Settings.getInstance().toString() + "\n" + infoTA.getText());


        }
//        picTable.getFocusModel().focus(-1);
//        picTable.getSelectionModel().clearAndSelect(0);
//        picTable.getFocusModel().focus(0);
    }


    public void clearTable() {
        List<List<Picture>> newPictList = new LinkedList<>();
        newPictList.addAll(pictList);
//            List list2 = ((List) ((ArrayList) list).clone());
//            ObservableList<Picture> clone = pictList.stream().collect(toList());

        picTable.getItems().clear();
        System.out.println(pictList);
        Picture.clearCount();
        pictList = newPictList;
        progressBar.setProgress(0);
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("result");
        alert.setHeaderText(message);
        alert.setContentText(":-|");

        alert.showAndWait();
    }

    @FXML
    private void showAlgorithmHistogram() {

        if (dimensionsChB.isSelected()) mainApp.showAlgorithmStatistics(pictList.get(0), "dimensions");
        if (distanceChB.isSelected()) mainApp.showAlgorithmStatistics(pictList.get(0), "distance");
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
