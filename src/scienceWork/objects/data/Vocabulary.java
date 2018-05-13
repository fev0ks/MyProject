package scienceWork.objects.data;

import org.opencv.core.Mat;
import scienceWork.objects.constants.FeatureTypes;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by mixa1 on 25.02.2018.
 */
public class Vocabulary {
    private Mat vocabulary;
    private int size;
    private int rows;
    private int cols;
    private int cvType;
    private int featureId;
    private int picCounts;
    private int countClusters;
    private long descriptors;
    private long createdDate;

    public Vocabulary() {

    }

    public Vocabulary(int rows, int cols, int cvType, int featureId, int size, int picCounts, long descriptors, long date, Mat vocabulary, int countClusters) {
        this.rows = rows;
        this.cols = cols;
        this.cvType = cvType;
        this.featureId = featureId;
        this.size = size;
        this.picCounts = picCounts;
        this.descriptors = descriptors;
        this.vocabulary = vocabulary;
        this.createdDate = date;
        this.countClusters = countClusters;
    }


    public Vocabulary(int rows, int cols, int cvType, int featureId, int size, int picCounts, long descriptors, Mat vocabulary, int countClusters) {
        this.rows = rows;
        this.cols = cols;
        this.cvType = cvType;
        this.featureId = featureId;
        this.size = size;
        this.picCounts = picCounts;
        this.descriptors = descriptors;
        this.vocabulary = vocabulary;
        this.createdDate = Calendar.getInstance().getTimeInMillis();
        this.countClusters = countClusters;
    }

    public int getCountClusters() {
        return countClusters;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public long getDescriptors() {
        return descriptors;
    }

    public Mat getMat() {
        return vocabulary;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getCvType() {
        return cvType;
    }

    public int getFeatureId() {
        return featureId;
    }

    public int getSize() {
        return size;
    }

    public int getPicCounts() {
        return picCounts;
    }

    public String getFormateddate() {
//        LocalTime.ofNanoOfDay(createdDate/10000000).toString();
//        LocalDate.ofEpochDay(createdDate).toString()
//        LocalDateTime.ofEpochSecond(createdDate/1000, (int)(createdDate%1000),ZoneOffset.UTC);
//        ZonedDateTime zdt1 = ZonedDateTime.of(LocalDateTime.ofEpochSecond(createdDate, ), ZoneId.of("Europe/Moscow"));
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate()
//                .format(DateTimeFormatter.RFC_1123_DATE_TIME);
        return    LocalDateTime.ofInstant(Instant.ofEpochMilli(createdDate),
                TimeZone.getDefault().toZoneId()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy H:m:s"));
    }

    @Override
    public String toString() {
        return "Type: " + FeatureTypes.getLabelById(featureId) +
                " Size: " + size +
                " Pictures: " + picCounts;
//                " \nDate: " + getFormateddate();

    }
}
