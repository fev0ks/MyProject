package scienceWork.objects;

import javafx.beans.property.*;
import scienceWork.objects.pictureData.DescriptorProperty;
import scienceWork.objects.pictureData.DimensionsProperty;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;

public class Picture {

    private static int count = 1;
    private File picFile;
    private String name;
    private String dir;
    private String pictureType;
    private String exitPictureType;
    private double size;
    private Dimension dimension;
    private LinkedList<Integer> groups;
    private DescriptorProperty descriptorProperty;
    private int countOfDescr;

    public Picture() {
        this.id = count++;
        groups = new LinkedList<>();
        groups.add(0);
        descriptorProperty = new DescriptorProperty();
        this.pictureType ="";
    }

    public int getCountOfDescr() {
        return countOfDescr;
    }

    public static void clearCount() {
        Picture.count = 1;
    }

    public void setCountOfDescr(int countOfDescr) {
        this.countOfDescr = countOfDescr;
    }

    public SimpleIntegerProperty getCountOfDescrProperty() {
        return new SimpleIntegerProperty(this.countOfDescr);
    }


    public DescriptorProperty getDescriptorProperty() {
        return descriptorProperty;
    }

    public void setDescriptorProperty(DescriptorProperty descriptorProperty) {
        this.descriptorProperty = descriptorProperty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public File getPicFile() {
        return picFile;
    }

    public void setPicFile(File picFile) {
        this.picFile = picFile;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public DimensionsProperty getDimensionsProperty() {
        return new DimensionsProperty(dimension);
    }

    public IntegerProperty getIdProperty() {
        return new SimpleIntegerProperty(this.id);
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(this.name);
    }

    public DoubleProperty getSizeProperty() {
        return new SimpleDoubleProperty(this.size);
    }

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public StringProperty getGroupProperty(){ return new SimpleStringProperty(this.exitPictureType);}
    public StringProperty getInpGroupProperty(){ return new SimpleStringProperty(this.pictureType);}

    public String getExitPictureType() {
        return exitPictureType;
    }

    public void setExitPictureType(String exitPictureType) {
        this.exitPictureType = exitPictureType;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "picFile=" + picFile +
                ", name='" + name + '\'' +
                ", dir='" + dir + '\'' +
                ", pictureType='" + pictureType + '\'' +
                ", groups=" + groups +
                '}';
    }
}
