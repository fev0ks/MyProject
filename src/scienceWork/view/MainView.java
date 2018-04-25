package scienceWork.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.FxWorker.ProgressImp;
import scienceWork.Main;
import scienceWork.Workers.FileWorker;
import scienceWork.algorithms.MainOperations;
import scienceWork.dataBase.VocabularyDB;
import scienceWork.objects.CommonML.AlgorithmML;
import scienceWork.objects.FeatureTypes;
import scienceWork.objects.LRInstance;
import scienceWork.objects.Picture;
import scienceWork.objects.SVMInstance;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.data.ImgTypesClusters;
import scienceWork.objects.pictureData.Directory;

import java.io.File;
import java.util.*;

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
    private Button initSVM;
    @FXML
    private Button stopBT;
    @FXML
    private Button loadVocabulary;
    @FXML
    private ListView<Integer> countThreadLV;
    @FXML
    private ListView<String> typeMethodKeyPandDescrLV;
    @FXML
    private ListView<Integer> countClustersLV;
    @FXML
    private Label settingsLbl;
    @FXML
    private RadioButton svmClassifierType;
    @FXML
    private RadioButton lrClassifierType;
    @FXML
    private RadioButton nnlassifierType;
    @FXML
    private ProgressIndicator progressIndicator;

    private List<Thread> threads;

    private int countPhotos;
    private Progress progress;
    private MainOperations mainOperations;
    private List<List<Picture>> pictLists;
    private String message = "";
    private AlgorithmML classifierAlgorithm;

    public void setDir(File dir) {
        directory = new Directory();
        directory.setDirFile(dir);
        directory.setDir(dir.getAbsolutePath());
        directory.setCountFiles(dir.listFiles().length);
//        message = managerDB.dirToDB(dir);
//        ProgressImp.getInstance().setProgressBar(progressBar);
        fileWorker.setProgressBar(progress);
        infoTA.setText("Loading pictures from " + directory.getDir());
        setDisabledButtons(true);
        Thread workFolderThread = new Thread(() -> {
            pictLists = fileWorker.loadPicFromDir(dir);

            // System.out.println(" files: " +dir.listFiles().length+" pic: "+ pictLists.size());
            countPhotos = pictLists.stream().mapToInt(List::size).sum();
            infoTA.setText(directory.getDir() + " files: " + dir.listFiles().length + " pic: " + countPhotos);
            updateTable();
            setDisabledButtons(false);
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
        progressIndicator.setProgress(-1f);
        stopBT.setVisible(false);
        progressIndicator.setVisible(false);
        progress = new ProgressImp(progressBar, infoTA);
        mainOperations = new MainOperations();
        threads = new ArrayList<>();
        System.out.println(FeatureTypes.getFeatureId(5));
        System.out.println(FeatureTypes.getLabel(5));

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
        System.out.println(" files: " + Objects.requireNonNull(directory.getDirFile().listFiles()).length + " pic: " + countPhotos);
        if (pictLists.size() > 0) {
            picTable.setItems(convertListsToObservableList(pictLists));
            inputType.setCellValueFactory(cellData -> cellData.getValue().getInpGroupProperty());
            groupColumn.setCellValueFactory(cellData -> cellData.getValue().getGroupProperty());
            namePicColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            sizePicColumn.setCellValueFactory(cellData -> cellData.getValue().getSizeProperty().asObject());
            countColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
            dimensionsColumn.setCellValueFactory(cellData -> cellData.getValue().getDimensionsProperty().toPropertyString());
//            countOfDescriptorsColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptorProperty().getCountOfDescrProperty().asObject());
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
            observableListPicures.addAll(pictList);
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
  @FXML
    private void showVocabularyMenu() {
        new Main().showVocabularyMenu();
//        infoTA.setText(Settings.getInstance().toString() + "\n" + infoTA.getText());
    }

    private void setDisabledButtons(boolean disable) {
        progressIndicator.setVisible(disable);
//        stopBT.setVisible(disable);
//        groupingBT.setDisable(disable);
//        analysisBT.setDisable(disable);
//        initClassifierData.setDisable(disable);
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
        Thread groupingThread = new Thread(() -> {
            if (classifierAlgorithm == null) {
                initClassifier(getSelectedClassifier());
            }
            mainOperations.executeClustering(pictLists, new ProgressImp(progressBar, infoTA), classifierAlgorithm);
            clearTable();
            updateTable();
            setDisabledButtons(false);
        });
        threads.add(groupingThread);
        groupingThread.start();
    }


    @FXML
    private void analysis() {
        setDisabledButtons(true);
        Thread analysisThread = new Thread(() -> {
            mainOperations.executeTraining(pictLists, new ProgressImp(progressBar, infoTA));
            clearTable();
            updateTable();
            setDisabledButtons(false);
        });
        threads.add(analysisThread);
        analysisThread.start();
    }

    @FXML
    private void createVocabulary() {
        setDisabledButtons(true);
        Thread vocabularyThread = new Thread(() -> {
//            for(int type = 2; type < 3; type++) {
//                if(type == 1){
//                    Settings.setMethodKP(12);
//                    Settings.setMethodDescr(7);
//                }
//                if(type == 2){
//                    Settings.setMethodKP(11);
//                    Settings.setMethodDescr(5);
//                }
//                for (int i = 500; i < 10000; i += 500) {
//                    Settings.setCountOfClusters(i);
                    mainOperations.executeInitVocabulary(pictLists, new ProgressImp(progressBar, infoTA));
                    VocabularyDB.getInstance().saveVocabulary(
                            pictLists.size(),
                            BOWVocabulary.vocabulary);
//                }

//            }
            setDisabledButtons(false);
        });
        threads.add(vocabularyThread);
        vocabularyThread.start();
    }

    @FXML
    private void initClassifierData() {
        setDisabledButtons(true);
        Thread classifierThread = new Thread(() -> {
            initClassifier(getSelectedClassifier());
            mainOperations.executeInitClassifier(new ProgressImp(progressBar, infoTA), classifierAlgorithm);
            setDisabledButtons(false);
        });
        threads.add(classifierThread);
        classifierThread.start();
    }

    private void initClassifier(int type) {

        if (type == 1) {
            classifierAlgorithm = SVMInstance.getSVMInstance();
        }
        if (type == 2) {
            classifierAlgorithm = LRInstance.getLRInstance();
        }
        if (type == 3) {

        }
    }

    private int getSelectedClassifier() {
        int type = 0;
        if (svmClassifierType.isSelected()) {
            type = 1;
        }
        if (lrClassifierType.isSelected()) {
            type = 2;
        }
        if (nnlassifierType.isSelected()) {
            type = 3;
        }
//        disablClassifiereRadiButtons(true);
        System.out.println("getSelectedClassifier " + type);
        return type;
    }

    private void disablClassifiereRadiButtons(boolean disable) {
        svmClassifierType.setDisable(disable);
        lrClassifierType.setDisable(disable);
        nnlassifierType.setDisable(disable);
    }

    @FXML
    private void selectRowInTable() {

        Picture pictureFromTable = picTable.getSelectionModel().getSelectedItem();
        if (pictureFromTable != null) {
            picTable.getSelectionModel().clearSelection();
            new Main().showInfoAboutPicture(pictureFromTable);
        }
//        picTable.getFocusModel().focus(-1);
//        picTable.getSelectionModel().clearAndSelect(0);
//        picTable.getFocusModel().focus(0);
    }

    @FXML
    /*doesn't work */
    private void stopThreads(){
        for(Thread thread: threads){
            if(thread.isAlive()){
                thread.stop();
            }
        }
        showMessage("Process was stopped!");
        setDisabledButtons(false);
        progressBar.setProgress(0);
    }

    void clearTable() {
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
