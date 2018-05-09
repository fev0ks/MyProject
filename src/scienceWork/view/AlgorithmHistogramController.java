package scienceWork.view;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import org.opencv.core.Mat;
import scienceWork.Main;
import scienceWork.algorithms.DescriptorProcess.KeyPointsAndDescriptors;
import scienceWork.algorithms.bow.bowTools.BOWImgDescriptorExtractor;
import scienceWork.objects.Picture;
import scienceWork.objects.data.BOWVocabulary;

import java.util.LinkedList;
import java.util.List;

public class AlgorithmHistogramController {
    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private BarChart<Integer, Double> barChart;

    @FXML
    private CategoryAxis xAxis;

    // private ObservableList monthNames = FXCollections.observableArrayList();

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл был загружен.
     */
    @FXML
    private void initialize() {
        // Получаем массив с английскими именами месяцев.
        //String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        // Преобразуем его в список и добавляем в наш ObservableList месяцев.
        // monthNames.addAll(Arrays.asList(months));
        // System.out.println(Arrays.asList(months).toString());
        // Назначаем имена месяцев категориями для горизонтальной оси.

    }

    public void setAlgorithmData(Picture picture) {
        barChart.setTitle("Vocabulary");
        //   xAxis.setCategories();
        // Считаем адресатов, имеющих дни рождения в указанном месяце.
//        for (Picture p : pictureList) {
//            int count = p.getBirthday().getMonthValue() - 1;
//            monthCounter[month]++;
//        }

        LinkedList<XYChart.Series<Integer, Double>> series = new LinkedList<XYChart.Series<Integer, Double>>();
        //set first bar color
        for (Node n : barChart.lookupAll(".default-color0.chart-bar")) {
            n.setStyle("-fx-bar-fill: red;");
        }
        //second bar color
        for (Node n : barChart.lookupAll(".default-color1.chart-bar")) {
            n.setStyle("-fx-bar-fill: green;");
        }

//        if (algName.equals("dimensions")) {
//            // Создаём объект XYChart.Data для каждого месяца.
//            // Добавляем его в серии.
//            for (int i = 0; i < 3; i++) {
//                XYChart.Series addSeries = new XYChart.Series();
//                series.add(addSeries);
//            }
//
//            series.get(0).setName("size");
//            series.get(1).setName("width");
//            series.get(2).setName("height");
//
//
//            for (Picture aPictureList : pictureList) {
//                series.get(0).getData().add(new XYChart.Data(String.valueOf(aPictureList.getId()), aPictureList.getSize()));
//                series.get(1).getData().add(new XYChart.Data(String.valueOf(aPictureList.getId()), aPictureList.getDimension().getWidth()));
//                series.get(2).getData().add(new XYChart.Data(String.valueOf(aPictureList.getId()), aPictureList.getDimension().getHeight()));
//            }
//        }


//        if (algName.equals("distance")) {
        if (BOWVocabulary.vocabulary != null) {
            try {
//                picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture));
//                long normalize = picture.getDescriptorProperty().getMatOfDescription().rows();
                XYChart.Series addSeries = new XYChart.Series();
                series.add(addSeries);
                series.get(0).setName("value");
                Mat mat = findVocabularyTemp(picture);

                for (int i = 0; i < mat.cols(); i++) {
//            for (Picture aPictureList : pictureList) {
                    series.get(0).getData().add(new XYChart.Data(i + " ", (mat.get(0, i)[0])));
                }
//        }  picture.setDescriptorProperty(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            barChart.getData().addAll(series);
        }

    }

    /*ehfyuhfiusdahiufgjdfiougjdsruigiorgoird FFFFFFFFFFFFFFFFFFFFFFFFF*/
    private Mat findVocabularyTemp(Picture picture) {
        BOWImgDescriptorExtractor extractor;
        extractor = BOWVocabulary.getBOWImgDescriptorExtractor();
        extractor.setVocabulary(BOWVocabulary.vocabulary.getMat());
        Mat outMat = new Mat();
//        MatOfKeyPoint keyPoints = picture.getDescriptorProperty().getMatOfKeyPoint();
        try {
//            if (keyPoints == null) {
//            picture.setDescriptorProperty(new KeyPointsAndDescriptors().calculateDescriptorProperty(picture)); //**********************
//            }
            Mat descriptors = picture.getDescriptorProperty().getMatOfDescription();
            List<List<Integer>> pointIdxsOfClusters = null;
            extractor.compute(descriptors, outMat, pointIdxsOfClusters);
//            picture.setDescriptorProperty(null);
//            System.gc();
        } catch (Exception e) {
            System.out.println(picture.toString());
            e.printStackTrace();
        }
        return outMat;
    }
}
