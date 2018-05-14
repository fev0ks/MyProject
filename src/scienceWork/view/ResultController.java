package scienceWork.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import scienceWork.FxWorker.FxHelper;
import scienceWork.Main;
import scienceWork.Workers.FileWorker;
import scienceWork.objects.GeneralPicturesInformation;
import scienceWork.objects.Picture;
import scienceWork.objects.data.BOWVocabulary;

import java.util.List;

import static scienceWork.FxWorker.FxHelper.showMessage;

public class ResultController {

    private Stage dialogStage;
    private List<List<Picture>> pictureLists;
    private Picture selectedPicture;
    private final String clearValue = "<empty>";
    @FXML
    private Button exitBT;
    @FXML
    private Button saveBT;
    @FXML
    private AnchorPane anchorPane;

    private ScrollPane scrollPane;

    @FXML
    private TableView<Picture> picTable;
    @FXML
    private TableColumn<Picture, String> namePicColumn;
    @FXML
    private TableColumn<Picture, String> typePicture;

    @FXML
    private ImageView imageView;

    @FXML
    private Slider sizePictureSlider;
    @FXML
    private ChoiceBox<String> pictureTypeCB;

    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @FXML
    private void updateTable() {
        if (pictureLists.size() > 0) {
            picTable.setItems(FxHelper.convertListsToObservableList(pictureLists));
            namePicColumn.setCellValueFactory(cellData -> FxHelper.convertStringToStringProperty(cellData.getValue().getName()));
            typePicture.setCellValueFactory(cellData -> FxHelper.convertStringToStringProperty(cellData.getValue().getExitPictureType()));
        } else {
            showMessage("Error", "There are no any pictures!", "Please load", Alert.AlertType.ERROR, mainApp);
            mainApp.showToolsScene();
        }
    }

    public void initDataPage(List<List<Picture>> pictureLists) {
        this.pictureLists = pictureLists;
//        pictureTypeCB.setAlignment(Pos.BASELINE_CENTER);
        setPictureTypesChoiceBox();
        updateTable();
        setFirstPictureToImageView();

        picTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
              selectRowInTable();
            }
        });
    }

    private void setPictureTypesChoiceBox() {

        ObservableList<String> types = FXCollections.observableArrayList();
        types.add(clearValue);
        types.addAll(FxHelper.convertListToObservableList(BOWVocabulary.getClustersTypes()));
        pictureTypeCB.setItems(types);
        pictureTypeCB.setValue(clearValue);
    }

    private void setFirstPictureToImageView() {
        selectedPicture = pictureLists.get(0).get(0);
        showPicture();
        picTable.getSelectionModel().focus(0);
        picTable.getSelectionModel().clearAndSelect(0);
    }

    @FXML
    public void selectRowInTable() {

        selectedPicture = picTable.getSelectionModel().getSelectedItem();
        if (selectedPicture != null) {
            imageView.setImage(null);
            showPicture();
        }
    }

    private void showPicture() {

        if (selectedPicture.getExitPictureType() != null) {
            pictureTypeCB.setValue(selectedPicture.getExitPictureType());
        }
        resizeImageView();
        setPictureToImageView();
    }

    public void resizeImageView(){
        setSizeImageView();
    }

    private void setPictureToImageView() {
        Platform.runLater(() -> imageView.setImage(getImage()));
    }


    private Image getImage() {
        try {
            return FxHelper.convertAwtImageToFxImage(FileWorker.getInstance().loadBufferedImage(selectedPicture.getPicFile()));
        } catch (Exception e) {
            FxHelper.showMessage("Error", "Failed load a picture", e.getCause().toString(), Alert.AlertType.ERROR, mainApp);
        }
        return null;
    }

    private void setSizeImageView() {
        double height = anchorPane.getHeight() == 0 ? anchorPane.getPrefHeight() : anchorPane.getHeight() - 2;
        double width = anchorPane.getWidth() == 0 ? anchorPane.getPrefWidth() : anchorPane.getWidth() - 2;
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);
    }


    @FXML
    public void saveTitle() {

        updateTypePictureIfNeeded();
        int nextRow = picTable.getSelectionModel().getFocusedIndex() + 1;
        if (nextRow < GeneralPicturesInformation.getInstance().getPictureCount()) {
            picTable.getSelectionModel().clearAndSelect(nextRow);
            picTable.getSelectionModel().focus(nextRow);
            picTable.getSelectionModel().clearAndSelect(nextRow);
            selectRowInTable();
        } else {
            FxHelper.showMessage("End of the table", "This was the last picture", "Select another picture or press exit", Alert.AlertType.INFORMATION, mainApp);
        }
    }

    private void updateTypePictureIfNeeded() {
        String savedType = pictureTypeCB.getValue();
        if (!savedType.equals(clearValue)) {
            if (!savedType.equals(selectedPicture.getExitPictureType())) {
                selectedPicture.setExitPictureType(savedType);
            }
        } else {
            selectedPicture.setExitPictureType("");
        }
    }

    @FXML
    public void closePage() {
        dialogStage.close();
    }


}
