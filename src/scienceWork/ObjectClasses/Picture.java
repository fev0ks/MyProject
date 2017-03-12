package scienceWork.ObjectClasses;

import javafx.beans.property.*;
import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

public class Picture {


    private  File picFile;
    private String name;
    private String dir;
    private double size;
    private Dimension dimension;
    private LinkedList<Integer> groups;
    private Mat imgMat;
    private Algorithms algorithms;
    private BufferedImage image;
    private static int count=1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public  File getPicFile() {
        return picFile;
    }

    public  void setPicFile(File picFile) {
        this.picFile = picFile;
    }
    public Dimension getDimension() {
        return dimension;
    }

    //TODO разрешение нужно запихать в сам BufferedImage
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }
    public DimensionsProperty getDimensionsProperty(){
        return new DimensionsProperty(dimension);
    }
    public Picture(String name) {
        this.name = name;
    }

    public Picture() {
        this.groups=new LinkedList<>();
        this.id=count++;
        groups.add(0);
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

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
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

    public StringProperty getNameProperty(){
        return new SimpleStringProperty(this.name);
    }
    public DoubleProperty getSizeProperty(){
        return new SimpleDoubleProperty(this.size);
    }
}
