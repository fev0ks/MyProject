package scienceWork.view;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import scienceWork.Main;
import scienceWork.objects.Picture;

import java.util.LinkedList;
import java.util.List;

public class AlgorithmHistogramController {
    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private BarChart barChart;

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

    public void setAlgorithmData(List<Picture> pictureList, String algName) {
        barChart.setTitle(algName);
        //   xAxis.setCategories();
        // Считаем адресатов, имеющих дни рождения в указанном месяце.
        int[] monthCounter = new int[12];
//        for (Picture p : pictureList) {
//            int count = p.getBirthday().getMonthValue() - 1;
//            monthCounter[month]++;
//        }

        LinkedList<XYChart.Series> series = new LinkedList<XYChart.Series>();
        //set first bar color
        for (Node n : barChart.lookupAll(".default-color0.chart-bar")) {
            n.setStyle("-fx-bar-fill: red;");
        }
        //second bar color
        for (Node n : barChart.lookupAll(".default-color1.chart-bar")) {
            n.setStyle("-fx-bar-fill: green;");
        }
        if (algName.equals("dimensions")) {
            // Создаём объект XYChart.Data для каждого месяца.
            // Добавляем его в серии.
            for (int i = 0; i < 3; i++) {
                XYChart.Series addSeries = new XYChart.Series();
                series.add(addSeries);
            }

            series.get(0).setName("size");
            series.get(1).setName("width");
            series.get(2).setName("height");


            for (Picture aPictureList : pictureList) {
                series.get(0).getData().add(new XYChart.Data(String.valueOf(aPictureList.getId()), aPictureList.getSize()));
                series.get(1).getData().add(new XYChart.Data(String.valueOf(aPictureList.getId()), aPictureList.getDimension().getWidth()));
                series.get(2).getData().add(new XYChart.Data(String.valueOf(aPictureList.getId()), aPictureList.getDimension().getHeight()));
            }
        }
        if (algName.equals("distance")) {
            XYChart.Series addSeries = new XYChart.Series();
            series.add(addSeries);
            series.get(0).setName("value");

//            for (Picture aPictureList : pictureList) {
//                series.get(0).getData().add(new XYChart.Data(String.getFeatureId(aPictureList.getName()), aPictureList.getValueAlgorithm().getNormKeyPoints()));
//            }
        }
        barChart.getData().addAll(series);

    }
}
