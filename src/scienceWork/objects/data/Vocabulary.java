package scienceWork.objects.data;

import org.opencv.core.Mat;
import scienceWork.objects.FeatureTypes;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mixa1 on 25.02.2018.
 */
public class Vocabulary {
    private Mat vocabulary;
    private int size;
    private long total;
    private int rows;
    private int cols;
    private int cvType;
    private int featureId;
    private int picCounts;
    private long descriptors;
    private long createdDate;

    public Vocabulary() {

    }

    public Vocabulary(long total, int rows, int cols, int cvType, int featureId, int size, int picCounts, long descriptors, long date, Mat vocabulary) {
        this.total = total;
        this.rows = rows;
        this.cols = cols;
        this.cvType = cvType;
        this.featureId = featureId;
        this.size = size;
        this.picCounts = picCounts;
        this.descriptors = descriptors;
        this.vocabulary = vocabulary;
        this.createdDate = date;
    }


    public Vocabulary(long total, int rows, int cols, int cvType, int featureId, int size, int picCounts, long descriptors, Mat vocabulary) {
        this.total = total;
        this.rows = rows;
        this.cols = cols;
        this.cvType = cvType;
        this.featureId = featureId;
        this.size = size;
        this.picCounts = picCounts;
        this.descriptors = descriptors;
        this.vocabulary = vocabulary;
        this.createdDate = Calendar.getInstance().getTimeInMillis();
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public long getDescriptors() {
        return descriptors;
    }

    public Mat getVocabulary() {
        return vocabulary;
    }

    public long getTotal() {
        return total;
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
                " Pictures: " + picCounts +
                " \nDate: " + getFormateddate();

    }
}
