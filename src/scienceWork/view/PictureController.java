package scienceWork.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import scienceWork.Main;
import scienceWork.Workers.PictureWorker;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.Picture;

import java.net.URL;
import java.util.ResourceBundle;

import static scienceWork.FxWorker.FxHelper.showMessage;

;

/**
 * Created by mixa1 on 05.12.2017.
 */
public class PictureController implements Initializable {
    private Stage dialogStage;
    private Picture picture;
    private int size;
    private boolean isShown = false;
    private Image image;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button showBt;

    private ScrollPane scrollPane;
    //    @FXML
//    private ChoiceBox<String> typeClusterCB;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea infoTA;
    @FXML
    private Slider sizePictureSlider;
    @FXML
    private CheckBox grayScaleCB;
    @FXML
    private CheckBox keyPointsCB;

    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void setPicture(Picture picture) {

        this.picture = picture;
        try {
            this.picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture));
        } catch (Exception e) {
            e.printStackTrace();
        }
        fillUpInfoAboutPicture();
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void showPicture() {
        System.out.println("showing: " + picture.getDir() + "\\" + picture.getName());
        getImage();
        setSizeImageView();
        setPictureToImageView();
    }

    @FXML
    private void resizePicture() {
        size = (int) Math.round(sizePictureSlider.getValue());
        setPictureToImageView();
        setSizeImageView();
    }

    private void setPictureToImageView() {
        Platform.runLater(() -> imageView.setImage(image));
    }

    private void getImage() {
        boolean isGrayScale = grayScaleCB.isSelected();
        boolean printKeyPoints = keyPointsCB.isSelected();
        try {
            image = PictureWorker.getImage(picture, isGrayScale, printKeyPoints);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillUpInfoAboutPicture() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Picture: ");
        stringBuilder.append(picture.getName());
        stringBuilder.append("\n");
        stringBuilder.append("Size: ");
        stringBuilder.append(picture.getSize());
        stringBuilder.append("\n");
        stringBuilder.append("Dim: ");
        stringBuilder.append(picture.getDimension().getHeight());
        stringBuilder.append("x");
        stringBuilder.append(picture.getDimension().width);
        stringBuilder.append("\n");
        stringBuilder.append("Features: ");
        stringBuilder.append(picture.getDescriptorProperty().getCountOfDescr());
        infoTA.setText(stringBuilder.toString());
    }

    public void initFirstWindow() {
        if (picture.getDescriptorProperty() == null) {
            try {
                picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setSizeImageView();
        getImage();
        imageView.setImage(image);
    }

    public void resizeWindowListener() {
        setSizeImageView();
    }

    @FXML
    private void clickAnchorPaint() {
        setSizeImageView();
    }

    private void setSizeImageView() {
        size = (int) Math.round(sizePictureSlider.getValue());
        double height = picture.getDimension().getHeight() * size / 100;
        double width = picture.getDimension().getWidth() * size / 100;
        scrollPane.setPrefSize(
                anchorPane.getWidth() == 0 ? anchorPane.getMinWidth() : anchorPane.getWidth(),
                anchorPane.getHeight() == 0 ? anchorPane.getMinHeight() : anchorPane.getHeight());
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        size = (int) Math.round(sizePictureSlider.getValue());
        scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(imageView);
        anchorPane.getChildren().add(scrollPane);

//        NumberBinding bindAnchorSize = Bindings.min(anchorPane.heightProperty(), anchorPane.widthProperty());
    }

    @FXML
    private void showAlgorithmHistogram() {
        if (BOWVocabulary.vocabulary.getMat() != null) {
            new Main().showAlgorithmStatistics(picture);
        } else {
            showMessage("Error", "Vocabulary is empty!", "Please load or create", Alert.AlertType.ERROR, mainApp);
        }
    }
}
