package scienceWork.algorithms;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import scienceWork.FileWorker;
import scienceWork.objects.Picture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class PictureWorker {
    public static Image printKP(Picture picture) {
        Mat outputImage = new Mat();
        Scalar color = new Scalar(0, 255, 255);
        int flags = Features2d.NOT_DRAW_SINGLE_POINTS;
        BufferedImage bufferedImage = FileWorker.getInstance().loadBufferedImage(picture.getPicFile());
        Features2d.drawKeypoints(getMatFromImage(bufferedImage), picture.getDescriptorProperty().getMatOfKeyPoint(), outputImage, color, flags);
        return convertMatToFXImage(outputImage);
    }

    private static Image convertMatToFXImage(Mat mat) {
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".bmp", mat, byteMat);
        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }

    //перевод изображения в формат OpenCV - Mat
    public static Mat getMatFromImage(BufferedImage image) {
        Mat imageMat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        imageMat.put(0, 0, data);
        return imageMat;
    }

    public static Image printClusters(Picture picture, Mat clusters) {
        ClusterTools clusterTools = new ClusterTools();
        BufferedImage bufferedImage = FileWorker.getInstance().loadBufferedImage(picture.getPicFile());

        Map<Integer, Integer> colorKP = new HashMap();
        for (int i = 0; i < picture.getDescriptorProperty().getCountOfDescr(); i++) {
            double minDistance = Double.MAX_VALUE;
            double distance;
            int numberCluster = -1;
            Mat clusterOfOwnPicture = picture.getDescriptorProperty().getMatOfDescription().row(i);
            for (int j = 0; j < clusters.height(); j++) {
                distance = clusterTools.findDistanceForMats(clusterOfOwnPicture, clusters.row(j));
                if (distance < minDistance) {
                    minDistance = distance;
                    numberCluster = j;
                }
            }
            colorKP.put(i, -numberCluster * (1000000/ clusters.height()));
        }
        int cluster = 0;
        for (KeyPoint keyPoints : picture.getDescriptorProperty().getMatOfKeyPoint().toArray()) {
//            System.out.println(cluster+" "+colorKP.get(cluster));
            printCircle(bufferedImage, (int) keyPoints.pt.x, (int) keyPoints.pt.y, colorKP.get(cluster));
            cluster++;
        }
        return SwingFXUtils.toFXImage(bufferedImage, null);

    }

    private static void printCircle(BufferedImage bufferedImage, int x, int y, int color) {
        bufferedImage.setRGB(x, y, color);
        bufferedImage.setRGB(x + 1, y, color);
        bufferedImage.setRGB(x, y + 1, color);
        bufferedImage.setRGB(x, y - 1, color);
        bufferedImage.setRGB(x - 1, y, color);
    }
}
