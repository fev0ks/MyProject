package scienceWork.Workers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.coobird.thumbnailator.Thumbnails;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.dataBase.ManagerDB;
import scienceWork.objects.Picture;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FileWorker {
    public static volatile FileWorker instance;
    public static double avrgH = 0;
    public static double avrgW = 0;
    public static int count = 0;
    private int numberDir = 0;
    private Progress progress;
    private long countOfFiles;
    private long number;

    private FileWorker() {
    }

    public static FileWorker getInstance() {
        if (instance == null)
            synchronized (ManagerDB.class) {
                if (instance == null)
                    instance = new FileWorker();
            }
        return instance;
    }

    public void setProgressBar(Progress progress) {
        this.progress = progress;
    }

    //загружаем фотографии из выбранной папки
    public List<List<Picture>> loadPicFromDir(File dir) {
        number = 0;
        countOfFiles = getCountOfFiles(dir);
        List<Picture> pictures = new LinkedList();
        List<List<Picture>> allPicturesFromDirectories = new LinkedList<>();
        for (File file : dir.listFiles()) {
            if (file.isFile()) { //фаил?
                if (isPicture(file)) { //картинка?
                    pictures.add(createPictureFromFile(file, dir.getName()));
                }
            } else {
                allPicturesFromDirectories.add(getPicturesFromDir(file));
            }
        }
        if(!pictures.isEmpty())
        allPicturesFromDirectories.add(pictures);
        if (progress != null) {
            progress.setProgress(0, countOfFiles);
        }
        return allPicturesFromDirectories;
    }

    private List<Picture> getPicturesFromDir(File dir) {
        List<Picture> pictures = new LinkedList();
        for (File file : dir.listFiles())
            if (isPicture(file)) { //картинка?
                pictures.add(createPictureFromFile(file, dir.getName()));
            }
        return pictures;
    }

    private Picture createPictureFromFile(File file, String pictureType) {
        if (progress != null) {
            progress.setProgress(number++, countOfFiles);
        }
        Picture picture = new Picture();
        picture.setName(file.getName());
        picture.setSize(file.length() / 1024);
        picture.setDir(file.getAbsolutePath());
        picture.setPicFile(file); //путь до фотки
        picture.setPictureType(pictureType);
        picture.setDimension(getPicDimensions(file));
        return picture;
    }

    private long getCountOfFiles(File file) {
        long count = 0;
        try {
            count = Files.walk(Paths.get(file.getAbsolutePath()))
                    .filter(Files::isRegularFile).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private boolean isPicture(File file) {
        String[] formatFile = file.getName().split("\\.");
        return formatFile.length > 0 && new MimetypesFileTypeMap().getContentType(file).contains("image");
//        return true;
    }


    private ObservableList<Picture> getPicuresFromFile(File file) {
        ObservableList<Picture> pictures = FXCollections.observableArrayList();

        return pictures;
    }

    public BufferedImage loadBufferedImage(File file) {
        BufferedImage loadImg = null;
        try {
//            System.out.println(file);
            loadImg = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resizeLoadBitmap(loadImg, 600);
    }

    public Image loadImage(File file) {
        Image image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    //уeменьшение больших изображений
    private BufferedImage resizeLoadBitmap(BufferedImage image, int compression) {
        BufferedImage newImage = image;
        int height = image.getHeight();
        int width = image.getWidth();
        if (height > compression || width > compression) {
            int w1 = width / compression, h1 = height / compression;
            int power = w1;
            if (h1 > w1) power = h1;
            try {
                newImage = Thumbnails.of(image).size(width / power, height / power).asBufferedImage();
                avrgH += newImage.getHeight();
                avrgW += newImage.getWidth();
                count++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return newImage;//если не надо уменьшать
    }

    //получить разрешение изображений
    private Dimension getPicDimensions(File pictureFile) {
        //   System.out.println(pictureFile);
        try (ImageInputStream in = ImageIO.createImageInputStream(pictureFile)) {
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
