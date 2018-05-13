package scienceWork.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import scienceWork.FxWorker.FxHelper;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.FxWorker.ProgressImp;
import scienceWork.Main;
import scienceWork.Workers.FileWorker;
import scienceWork.MainOperations;
import scienceWork.objects.GeneralPicturesInformation;
import scienceWork.objects.machineLearning.ANNMLPInstance;
import scienceWork.objects.machineLearning.CommonML.AlgorithmML;
import scienceWork.objects.constants.FeatureTypes;
import scienceWork.objects.machineLearning.LRInstance;
import scienceWork.objects.Picture;
import scienceWork.objects.machineLearning.SVMInstance;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.machineLearning.SVMSGDInstance;
import scienceWork.objects.pictureData.Directory;

import java.io.File;
import java.util.*;

import static scienceWork.FxWorker.FxHelper.showMessage;

public class MainController {
    private Main mainApp;
    private Stage dialogStage;
    private Directory directory;
    private String typeImages;

    private final FileWorker fileWorker = FileWorker.getInstance();

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
    private ProgressBar progressBar;
    @FXML
    private Button groupingBT;
    @FXML
    private Button analysisBT;
    @FXML
    private Button newDirBT;
    @FXML
    private Button resetDataBT;
    @FXML
    private Button createVocabulary;
    @FXML
    private Button initSVM;
    @FXML
    private Button clearBT;
    @FXML
    private Button loadVocabulary;
    @FXML
    private RadioButton svmClassifierType;
    @FXML
    private RadioButton svmsgdClassifierType;
    @FXML
    private RadioButton lrClassifierType;
    @FXML
    private RadioButton annMplClassifierType;
    @FXML
    private RadioButton nnlassifierType;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Button saveGroups;

//    private List<Thread> threads;

    private Progress progress;
    private MainOperations mainOperations;
    private List<List<Picture>> pictLists;
    private AlgorithmML classifierAlgorithm;


    public void setDir(File dir) {
        directory = new Directory();
        directory.setDirFile(dir);
        directory.setDir(dir.getAbsolutePath());
        directory.setCountFiles(dir.listFiles().length);
//        message = managerDB.dirToDB(dir);
//        ProgressImp.getInstance().setProgressBar(progressBar);
        fileWorker.setProgressBar(progress);
//        infoTA.setText("Loading pictures from " + directory.getDir());
        setDisabledButtons(true);
        Thread workFolderThread = new Thread(() -> {
            pictLists = fileWorker.loadPicFromDir(dir);

            // System.out.println(" files: " +dir.listFiles().length+" pic: "+ pictLists.size());
            GeneralPicturesInformation.getInstance().setPictureCount(pictLists.stream().mapToInt(List::size).sum());
            infoTA.setText(directory.getDir() + " files: " + dir.listFiles().length + " pic: " + GeneralPicturesInformation.getInstance().getPictureCount() + "\n" + infoTA.getText());
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
//        clearBT.setVisible(false);
        progressIndicator.setVisible(false);
        progress = new ProgressImp(progressBar, infoTA);
        mainOperations = new MainOperations();
//        threads = new ArrayList<>();
    }

    @FXML
    private void resetData() {
        BOWVocabulary.vocabulary = null;
        BOWVocabulary.vocabularies = null;
        classifierAlgorithm = null;
        infoTA.setText("Vocabulary and classifier were cleared");
    }

    @FXML
    private void choseNewDirectory() {

        File file = showDirectorySelector();
        if (file != null) {
            setDir(file);
            clearTable();
        }
        if (file == null && pictLists.isEmpty()) {
            System.exit(0);
        }

    }

    private File showDirectorySelector() {
        return new Main().showChooseDir();
    }

    //вывожу данные об изображениях в табилцу
    @FXML
    private void updateTable() {
        System.out.println(" folders: " + Objects.requireNonNull(directory.getDirFile().listFiles()).length + " pic: " + GeneralPicturesInformation.getInstance().getPictureCount());
        if (pictLists.size() > 0) {

            picTable.setItems(FxHelper.convertListsToObservableList(pictLists));

            countColumn.setCellValueFactory(cellData -> FxHelper.convertIntegerToSimpleIntegerProperty(cellData.getValue().getId()).asObject());
            namePicColumn.setCellValueFactory(cellData -> FxHelper.convertStringToStringProperty(cellData.getValue().getName()));
            inputType.setCellValueFactory(cellData -> FxHelper.convertStringToStringProperty(cellData.getValue().getPictureType()));
            groupColumn.setCellValueFactory(cellData -> FxHelper.convertStringToStringProperty(cellData.getValue().getExitPictureType()));
            sizePicColumn.setCellValueFactory(cellData -> FxHelper.convertDoubleToDoubleProperty(cellData.getValue().getSize()).asObject());
            dimensionsColumn.setCellValueFactory(cellData -> cellData.getValue().getDimensionsProperty().toPropertyString());
//            countOfDescriptorsColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptorProperty().getCountOfDescrProperty().asObject());
//            System.out.println(pictLists.get(0).getDescriptorProperty().getCountOfDescr() + " descr");
//            dimensionsColumn.setCellValueFactory(cellData -> cellData.getValue().getDimensionsProperty().toPropertyString());
        } else {
            showMessage("Error", "There are no any images!", "Please select another folder", Alert.AlertType.ERROR, mainApp);
            mainApp.showToolsScene();
        }
//        showMessage(message);
        loadPicToDB();
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
        mainApp.showVocabularyMenu();
//        infoTA.setText(Settings.getInstance().toString() + "\n" + infoTA.getText());
    }

    private void setDisabledButtons(boolean disable) {
        progressIndicator.setVisible(disable);
//        clearBT.setVisible(disable);
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
        if (mainOperations.checkExistMLInstance(classifierAlgorithm)) {
            setDisabledButtons(true);
            Thread groupingThread = new Thread(() -> {
//                if (classifierAlgorithm == null) {
//                    initSelectedClassifier();
//                    classifierAlgorithm.setCountClusters(pictLists.size());
//                    classifierAlgorithm.setFeatureID(FeatureTypes.getFeatureId(Settings.getMethodKP()));
//                }
                mainOperations.executeClustering(pictLists, new ProgressImp(progressBar, infoTA), classifierAlgorithm);
                clearTable();
                updateTable();
                setDisabledButtons(false);
            });
//            threads.add(groupingThread);
            groupingThread.start();
        }
    }


    @FXML
    private void analysis() {

        if (mainOperations.checkExistVocabulary()) {
            setDisabledButtons(true);
            Thread analysisThread = new Thread(() -> {
                mainOperations.executeTraining(pictLists, new ProgressImp(progressBar, infoTA));
                clearTable();
                updateTable();
                setDisabledButtons(false);
            });
//            threads.add(analysisThread);
            analysisThread.start();
        }
    }

    @FXML
    private void createVocabulary() {
        if (mainOperations.checkLoadedPictures(pictLists)) {
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
//                for (int i = 1500; i < 5000; i += 500) {
//                    Settings.setCountOfClusters(i);
                mainOperations.executeInitVocabulary(pictLists, new ProgressImp(progressBar, infoTA));
//                }

//            }
                setDisabledButtons(false);
            });
//        threads.add(vocabularyThread);
            vocabularyThread.start();
        }
    }

    @FXML
    private void initClassifierData() {

        if (mainOperations.checkExistVocabulary()) {
            if (mainOperations.checkExistTrainData()) {
                setDisabledButtons(true);
                Thread classifierThread = new Thread(() -> {
                    initSelectedClassifier();
                    classifierAlgorithm.setCountClusters(pictLists.size());
                    classifierAlgorithm.setFeatureID(FeatureTypes.getFeatureId(Settings.getMethodKP()));
                    mainOperations.executeInitClassifier(new ProgressImp(progressBar, infoTA), classifierAlgorithm);

                    setDisabledButtons(false);
                });
//                threads.add(classifierThread);
                classifierThread.start();
            }
        }
    }

    @FXML
    private void initSaveGroups() {
        setDisabledButtons(true);
        File file = showDirectorySelector();
        Thread saveGroupsThread = new Thread(() -> {
            if (file != null) {
                mainOperations.executeSavingGroups(pictLists, file, progress);
            }
            setDisabledButtons(false);
        });
        saveGroupsThread.start();
    }

    @FXML
    private void checkResults() {
        if (mainOperations.checkLoadedPictures(pictLists)) {
            mainApp.showResultPage(pictLists);
            updateTable();
        }
    }


    private void initSelectedClassifier() {
        if (svmClassifierType.isSelected()) {
            classifierAlgorithm = SVMInstance.getSVMInstance();
        }
        if (svmsgdClassifierType.isSelected()) {
            progress.addMessage("\nSVM SGD doesn't work using 3.4.1 OpenCv :(((");
            classifierAlgorithm = SVMSGDInstance.getSVMSGDInstance();
        }
        if (lrClassifierType.isSelected()) {
            classifierAlgorithm = LRInstance.getLRInstance();
        }
        if (annMplClassifierType.isSelected()) {
            classifierAlgorithm = ANNMLPInstance.getANNMLPInstance();
        }
//        System.out.println(classifierAlgorithm.toString());
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
            mainApp.showInfoAboutPicture(pictureFromTable);
        }
//        picTable.getFocusModel().focus(-1);
//        picTable.getSelectionModel().clearAndSelect(0);
//        picTable.getFocusModel().focus(0);
    }

    @FXML
    private void clearTextArea() {
        infoTA.setText("");
//        for(Thread thread: threads){
//            if(thread.isAlive()){
//                thread.stop();
//            }
//        }
//        showMessage("Process was stopped!");
//        setDisabledButtons(false);
//        progressBar.setProgress(0);
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


    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


}
