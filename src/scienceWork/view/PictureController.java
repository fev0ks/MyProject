package scienceWork.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import scienceWork.objects.picTypesData.ImgTypesClusters;
import scienceWork.objects.Picture;
import scienceWork.Workers.FileWorker;

import java.net.URL;
import java.util.ResourceBundle;

import static scienceWork.Workers.PictureWorker.printClusters;

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
    @FXML
    private ChoiceBox<String> typeClusterCB;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea infoTA;
    @FXML
    private Slider sizePictureSlider;


    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void showPicture() {
        System.out.println("showing: " + picture.getDir() + "\\" + picture.getName());
        selectTypeImageView();
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

//            Platform.runLater(() -> imageView.setImage(printKP(picture)));
        Platform.runLater(() -> imageView.setImage(image));
//            imageView.setImage(new Image("file:///" + picture.getDir() + "\\" + picture.getName(), width, height, false, false));
    }

    private void selectTypeImageView() {
        String selectedType = typeClusterCB.getSelectionModel().getSelectedItem();
        if (selectedType == null) {
            System.out.println("clear");
            image = SwingFXUtils.toFXImage(FileWorker.getInstance().loadBufferedImage(picture.getPicFile()), null);
        } else if (selectedType.equals("own kep points")) {
            System.out.println("own");
            image = printClusters(picture, picture.getDescriptorProperty().getCentersOfDescriptors());
        } else {
            System.out.println("selected type");
            image = printClusters(picture, ImgTypesClusters.trainedClusters.get(selectedType));
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
        stringBuilder.append("Descr-s: ");
        stringBuilder.append(picture.getDescriptorProperty().getCountOfDescr());
        infoTA.setText(stringBuilder.toString());
    }

    public void initFirstWindow() {
        if (picture.getDescriptorProperty().getMatOfDescription() == null) {
            showBt.setDisable(true);
        } else {
            typeClusterCB.getItems().add(1, "own kep points");
        }

        setSizeImageView();
        fillUpInfoAboutPicture();
        image = SwingFXUtils.toFXImage(FileWorker.getInstance().loadBufferedImage(picture.getPicFile()), null);
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
//        System.out.println("anchorPane "+anchorPane.getMaxHeight()+"x"+anchorPane.getMaxHeight());
//        System.out.println("anchorPane "+anchorPane.getLayoutX()+"x"+anchorPane.getLayoutY());
//        System.out.println("imageView "+height+"x"+width);
        imageView.setPreserveRatio(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeClusterCB.setItems(FXCollections.observableArrayList(ImgTypesClusters.getClustersTypes()));
        typeClusterCB.getItems().add(0, null);
        size = (int) Math.round(sizePictureSlider.getValue());
        scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(imageView);
        anchorPane.getChildren().add(scrollPane);

//        NumberBinding bindAnchorSize = Bindings.min(anchorPane.heightProperty(), anchorPane.widthProperty());
    }
}
