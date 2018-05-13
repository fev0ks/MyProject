package scienceWork.Workers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.coobird.thumbnailator.Thumbnails;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.dataBase.ManagerDB;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
//import java.awt.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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
        if (!pictures.isEmpty())
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


//    private ObservableList<Picture> getPicuresFromFile(File file) {
//        ObservableList<Picture> pictures = FXCollections.observableArrayList();
//
//        return pictures;
//    }

    public BufferedImage loadBufferedImage(File file) {
        BufferedImage loadImg = null;
        try {
            loadImg = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadImg;
    }
//
//    public BufferedImage loadGrayScaleBufferedImage(File file) {
//        BufferedImage grayImage = null;
//        try {
//            BufferedImage loadImg = ImageIO.read(file);
//            byte[] data = ((DataBufferByte) loadImg.getRaster().getDataBuffer()).getData();
//            Mat mat = new Mat(loadImg.getHeight(), loadImg.getWidth(), CvType.CV_8UC3);
//            mat.put(0, 0, data);
//
//            Mat mat1 = new Mat(loadImg.getHeight(), loadImg.getWidth(), CvType.CV_8UC1);
//            Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);
//
//            byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int) (mat1.elemSize())];
//            mat1.get(0, 0, data1);
//            grayImage = new BufferedImage(mat1.cols(), mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
//            grayImage.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return resizeLoadBitmap(grayImage);
//    }

    public Mat getMatFromFileOfImage(File file, boolean grayScale, boolean resize) {
        BufferedImage image = null;
        Mat imageMat = null;
        try {
            image = ImageIO.read(file);
            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            imageMat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
            imageMat.put(0, 0, data);
            if (grayScale) {
                Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_RGB2GRAY);
            }

            if (resize) {
                imageMat = resizeImageMat(imageMat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageMat;
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

    private Mat resizeImageMat(Mat imageMat) {
        int compression = Settings.getScaleImageRatio();
        double height = imageMat.rows();
        double width = imageMat.cols();
        if (height > compression || width > compression) {
            double w1 = (width / compression), h1 = height / compression;
            double power = w1;
            if (h1 > w1) power = h1;
            Imgproc.resize(imageMat, imageMat, new Size(width / power, height / power));
            count++;
        }

        return imageMat;//если не надо уменьшать
    }

//    public Mat getMatFromFileOfImage(File file, boolean grayScale, boolean resize) {
//        BufferedImage loadImg = null;
//        try {
////            System.out.println(file);
//            loadImg = ImageIO.read(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return resizeLoadBitmap(loadImg, 600);
//    }
//
//    private Mat resizeLoadBitmap(BufferedImage image, int compression) {
//        BufferedImage newImage = image;
//        int height = image.getHeight();
//        int width = image.getWidth();
//        if (height > compression || width > compression) {
//            int w1 = width / compression, h1 = height / compression;
//            int power = w1;
//            if (h1 > w1) power = h1;
//            try {
//                newImage = Thumbnails.of(image).size(width / power, height / power).asBufferedImage();
//                avrgH += newImage.getHeight();
//                avrgW += newImage.getWidth();
//                count++;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        Mat imageMat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
//        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//        imageMat.put(0, 0, data);
//
//        return imageMat;//если не надо уменьшать
//    }


    public void saveImageToGroups(Picture picture, File file, String temp) throws IOException {

        String pictureGroup = picture.getExitPictureType();
        if (!pictureGroup.isEmpty()) {
            String picturePath = "\\grouping " + temp + "\\" + pictureGroup + "\\";
            File dir = file == null ? new File(picture.getPicFile().getParent() + picturePath) : new File(file.getAbsolutePath() + picturePath);
            String newFileName = dir + "\\" + picture.getName();

            boolean existDirectory = dir.exists();

            if (!existDirectory) {
                existDirectory = dir.mkdir();
                if (!existDirectory) {
                    existDirectory = dir.mkdirs();
                }
            }

            BufferedImage loadImg = ImageIO.read(picture.getPicFile());
            ImageIO.write(loadImg, "jpg", new File(newFileName));

            System.out.println(newFileName);

        }
    }

//    //уменьшение больших изображений
//    private BufferedImage resizeLoadBitmap(BufferedImage image) {
//        int compression = Settings.getScaleImageRatio();
//        BufferedImage newImage = image;
//        double height = image.getHeight();
//        double width = image.getWidth();
//        if (height > compression || width > compression) {
//            double w1 = (width / compression), h1 = height / compression;
//            double power = w1;
//            if (h1 > w1) power = h1;
//            try {
//                newImage = Thumbnails.of(image).size((int) (width / power), (int) (height / power)).asBufferedImage();
//                avrgH += newImage.getHeight();
//                avrgW += newImage.getWidth();
//                count++;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return newImage;//если не надо уменьшать
//    }

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
