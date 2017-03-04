package scienceWork;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scienceWork.ObjectClasses.Picture;
import scienceWork.dataBase.ManagerDB;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class WorkFolder {
    public static volatile WorkFolder instance;

    private WorkFolder() {
    }

    public static WorkFolder getInstance() {
        if (instance == null)
            synchronized (ManagerDB.class) {
                if (instance == null)
                    instance = new WorkFolder();
            }
        return instance;
    }
    public ObservableList<Picture> loadPicFromDir(File dir) {

        ObservableList<Picture> pictList= FXCollections.observableArrayList();
        for (File file : dir.listFiles()) {
           // System.out.println(file.getName());
            if (file.isFile()) { //фаил?
                String[] formatFile = file.getName().split("\\.");
                Boolean isImage = false;

                if (formatFile.length > 0) {
                    String mimetype = new MimetypesFileTypeMap().getContentType(file);
                    isImage = mimetype.contains("image");
                }

                if (isImage) { //картинка?
                    Picture picture = new Picture();
//                    String nameFile = "";
//                    for (int i = 0; i < formatFile.length - 1; i++) {
//                        nameFile += formatFile[i];
//                        if (i + 1 != formatFile.length - 1) nameFile += ".";
//                    }
                    System.out.println(file.getName());
                    picture.setName(file.getName());
                    picture.setSize(file.length()/1024);
                    picture.setDir(dir.getAbsolutePath());
                    picture.setPicFile(file);//??????

                   // System.out.println(nameFile);
                    //   loadBitmap = resizeLoadBitmap(loadBitmap, MainActivity.compression);//уменьшаю загружаемый Bitmap
                    // bitmap.setPath(fileFinal.getAbsolutePath().substring(0, fileFinal.getAbsolutePath().lastIndexOf("/")));

                    // bitmap = ImageProcessing.INSTANCE.checkingForAFile(bitmap, MainActivity.methodKPStr, MainActivity.methodDescrStr);

                    //    Log.e(LOG_TAG, "bitmapIn.group==0 " + bitmap.getName());
                    //  return bitmap;
                    //  bitmapLoadArray.add(bitmap);
                    pictList.add(picture);
                }
            }
        }
        return pictList;
    }
    public BufferedImage loadPicFromFile(File file){
        BufferedImage loadImg = null;
        try {
            loadImg = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadImg;
    }

    public Dimension getPicDimensions(File pictureFile){
     //   System.out.println(pictureFile);
        try(ImageInputStream in = ImageIO.createImageInputStream(pictureFile)){
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    reader.dispose();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
