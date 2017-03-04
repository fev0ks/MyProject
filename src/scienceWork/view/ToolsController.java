package scienceWork.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import scienceWork.Main;
import scienceWork.ObjectClasses.Algorithms;
import scienceWork.ObjectClasses.Directory;
import scienceWork.ObjectClasses.Picture;
import scienceWork.WorkFolder;
import scienceWork.dataBase.ManagerDB;

import java.io.File;

public class ToolsController {
    private Main mainApp;
    private Directory directory;
    private final WorkFolder workFolder=WorkFolder.getInstance();
    private final ManagerDB managerDB=ManagerDB.getInstance();

    @FXML
    private TextField infoTF;
    @FXML
    private TableView<Picture> picTable;
    @FXML
    private TableColumn<Picture, Integer> countColumn;
    @FXML
    private TableColumn<Picture, String> namePicColumn;
    @FXML
    private TableColumn<Picture, Double> sizePicColumn;
    @FXML
    private TableColumn<Picture,String> someThingColumn;
    @FXML
    private CheckBox dimensionsChB;
    private ObservableList<Picture> pictList;
    private String message="";

    public void setDir(File dir) {
        directory=new Directory();
        directory.setDirFile(dir);
        directory.setDir(dir.getAbsolutePath());
        directory.setCountFiles(dir.listFiles().length);
        message= managerDB.dirToDB(dir);
        pictList=workFolder.loadPicFromDir(dir);
       // System.out.println(" files: " +dir.listFiles().length+" pic: "+ pictList.size());
        infoTF.setText(directory.getDir() + " files: " +dir.listFiles().length+" pic: "+ pictList.size());
        loadPicToTable();
    }
    @FXML
    private void loadPicToTable() {
        System.out.println(" files: " +directory.getDirFile().listFiles().length+" pic: "+ pictList.size());
        if(pictList.size()!=0) {
            System.out.println(pictList.get(0).getName());
            picTable.setItems(pictList);
            namePicColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            sizePicColumn.setCellValueFactory(cellData -> cellData.getValue().getSizeProperty().asObject());
            countColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        }else{
            showMessage("NO IMAGES!!!");
            mainApp.showToolsScene();
        }
        loadPicToDB();
    }
    private void loadPicToDB(){
        message+=(managerDB.picToDBfromDir(pictList));
        showMessage(message);
    }
    @FXML
    private void setPicDimensions(){
    }
    @FXML
    private void analise(){
        Algorithms algorithms=Algorithms.getInstance();
        if(dimensionsChB.isSelected()) {
            pictList = algorithms.setPicDimensions(pictList);
            someThingColumn.setText("Size");
            System.out.println(pictList.get(0).getDimension().toString());
            someThingColumn.setCellValueFactory(cellData -> cellData.getValue().getDimensionsProperty().toPropertyString());
        }
        picTable.refresh();
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
    private void showAlgorithmHistogram(){
        mainApp.showAlgorithmStatistics(pictList,"Dimensions");
    }
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
