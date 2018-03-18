package scienceWork.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.lang.time.StopWatch;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.FxWorker.UpdateProgressBar;
import scienceWork.Main;
import scienceWork.Workers.FileWorker;
import scienceWork.algorithms.bow.BOWClusterer;
import scienceWork.algorithms.bow.BOWTeacher;
import scienceWork.algorithms.bow.bowTools.BOWHelper;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.picTypesData.BOWVocabulary;
import scienceWork.objects.picTypesData.ImgTypesClusters;
import scienceWork.objects.pictureData.Directory;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainView {
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
    private TableColumn<Picture, String> inputType;
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
    private Button createVocabulary;
    @FXML
    private ListView<Integer> countThreadLV;
    @FXML
    private ListView<String> typeMethodKeyPandDescrLV;
    @FXML
    private ListView<Integer> countClustersLV;
    @FXML
    private Label settingsLbl;

    private Progress progress;

    private List<List<Picture>> pictLists;
    private String message = "";

    public void setDir(File dir) {
        directory = new Directory();
        directory.setDirFile(dir);
        directory.setDir(dir.getAbsolutePath());
        directory.setCountFiles(dir.listFiles().length);
//        message = managerDB.dirToDB(dir);
//        UpdateProgressBar.getInstance().setProgressBar(progressBar);
        fileWorker.setProgressBar(progress);
        infoTA.setText("Loading pictures from " + directory.getDir());
        Thread workFolderThread = new Thread(() -> {
            pictLists = fileWorker.loadPicFromDir(dir);

            // System.out.println(" files: " +dir.listFiles().length+" pic: "+ pictLists.size());
            infoTA.setText(directory.getDir() + " files: " + dir.listFiles().length + " pic: " + pictLists.size());
            updateTable();
        });
        workFolderThread.start();
    }

//    public Directory getDir() {
//        return directory;
//    }

//    public ProgressBar getProgressBar() {
//        return progressBar;
//    }


    @FXML
    public void initialize() {
        progress = new UpdateProgressBar(progressBar);
    }

    @FXML
    private void resetData() {
        infoTA.setText("Clearing " + ImgTypesClusters.trainedClusters.size() + " type(s) of pictures");
        ImgTypesClusters.trainedClusters = new HashMap<>();
    }

    @FXML
    private void choseNewDirectory() {
//        new Main().showToolsScene();
//    }
//        MainView controller = new MainView();
//        MainView controller = loader.getController();
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
        System.out.println(" files: " + directory.getDirFile().listFiles().length + " pic: " + pictLists.size());
        if (pictLists.size() > 0) {
            picTable.setItems(convertListsToObservableList(pictLists));
            inputType.setCellValueFactory(cellData -> cellData.getValue().getInpGroupProperty());
            groupColumn.setCellValueFactory(cellData -> cellData.getValue().getGroupProperty());
            namePicColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            sizePicColumn.setCellValueFactory(cellData -> cellData.getValue().getSizeProperty().asObject());
            countColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
            dimensionsColumn.setCellValueFactory(cellData -> cellData.getValue().getDimensionsProperty().toPropertyString());
            countOfDescriptorsColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptorProperty().getCountOfDescrProperty().asObject());
//            System.out.println(pictLists.get(0).getDescriptorProperty().getCountOfDescr() + " descr");
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
//        message += (managerDB.picToDBfromDir(pictLists));
    }


    @FXML
    private void showSettingsMenu() {
        new Main().showSettingsMenu();
        infoTA.setText(Settings.getInstance().toString() + "\n" + infoTA.getText());
    }

    private void setDisabledButtons(boolean disable) {
//        groupingBT.setDisable(disable);
//        analysisBT.setDisable(disable);
//        newDirBT.setDisable(disable);
//        showHistogramBT.setDisable(disable);
//        resetDataBT.setDisable(disable);
//        createVocabulary.setDisable(disable);
    }

    /*
    Группировка изображений
     */
    @FXML
    private void groupingImagesToClasses() {

        setDisabledButtons(true);

        Thread mainThread = new Thread(() -> {
            int count = 0;
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            for (List<Picture> pictList : pictLists) {

                new BOWClusterer(progress).findPictureType(pictList);
//            clearTable();
//            updateTable();

                count += pictList.size();
            }
            stopWatch.stop();
            viewWorkTime(stopWatch.getTime(), "Grouping");
            long right = pictLists.stream().map(l -> l.stream().filter(p -> p.getPictureType().equals(p.getExitPictureType())).count()).mapToLong(s -> s).sum();
            infoTA.setText("Right: " + right + "; False: " + (count - right) + "; Rate: " + ((double) right / count * 100) + "%\n" + infoTA.getText());
            clearTable();
            updateTable();
            setDisabledButtons(false);
        });
        mainThread.start();
    }


    @FXML
    private void analysis() {

//        typeImages = new Main().setNameOfTypeImages();
//        if (StringUtils.isBlank(typeImages)) {
//            infoTA.setText("Select type of analysing images\n" + infoTA.getText());
//        } else {
//            infoTA.setText("Analysing type: " + typeImages + "\n" + infoTA.getText());
        setDisabledButtons(true);
        Thread mainThread = new Thread(() -> {
            StopWatch stopWatchAll = new StopWatch();
            stopWatchAll.start();
            System.out.println(pictLists.size());
            for (List<Picture> pictList : pictLists) {
                System.out.println("analysis " + pictList.size());
                System.out.println("analysis " + pictList.get(0).toString());
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                new BOWTeacher(pictList, progress).findFeatures();
                stopWatch.stop();
                viewWorkTime(stopWatch.getTime(), pictList.get(0).getPictureType());
            }
            stopWatchAll.stop();
            viewWorkTime(stopWatchAll.getTime(), "Total time for " + pictLists.size() + " groups");

            clearTable();
            updateTable();
            setDisabledButtons(false);
        });
        mainThread.start();
//        }
    }

    @FXML
    private void createVocabulary() {
        Thread mainThread = new Thread(() -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            new BOWHelper(progress).createVocabulary(pictLists);
            stopWatch.stop();
            viewWorkTime(stopWatch.getTime(),  "Vocabulary size "+ BOWVocabulary.commonVocabulary.size());
        });
        mainThread.start();
    }

    private void viewWorkTime(long finishTime, String title) {
        System.out.println(title + "; Время: " + finishTime / 1000 + " сек, " + finishTime % 1000 + " мс;");
        message = "Завершено за " + finishTime / 1000 + " сек, " + finishTime % 1000 + " мс;";
        infoTA.setText(title + "; " + message + "\n" + infoTA.getText());
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
        newPictList.addAll(pictLists);
//            List list2 = ((List) ((ArrayList) list).clone());
//            ObservableList<Picture> clone = pictLists.stream().collect(toList());

        picTable.getItems().clear();
//        System.out.println(pictLists);
        Picture.clearCount();
        pictLists = newPictList;
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

        if (dimensionsChB.isSelected()) mainApp.showAlgorithmStatistics(pictLists.get(0), "dimensions");
        if (distanceChB.isSelected()) mainApp.showAlgorithmStatistics(pictLists.get(0), "distance");
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }


}
