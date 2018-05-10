package scienceWork.algorithms.DescriptorProcess;

import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.DescriptorsOfPictureClustering;
import scienceWork.objects.Picture;
import scienceWork.objects.constants.Settings;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by mixa1 on 21.02.2018.
 */
public class PictureClusters {

    //найти особые точки их дескрипторы, по ним найти центры кластеров дескрипторов изображения
    public void findPicturesClusters(List<Picture> pictList, Progress progress) {
//        ExecutorService executor = Executors.newFixedThreadPool(Settings.getCountThreads());
        ExecutorService executor = Executors.newFixedThreadPool(Settings.getInstance().getCountThreads());

        List<Future> futureList = new LinkedList<>();
        System.out.println("start find KP/Descr");
        int number = 0;
        for (Picture picture : pictList) {
            DescriptorsOfPictureClustering descriptorsOfPictureClustering = new DescriptorsOfPictureClustering(picture);
            try {
                futureList.add(executor.submit(descriptorsOfPictureClustering));
                descriptorsOfPictureClustering.run();
                progress.setProgress( number++ , pictList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < pictList.size(); i++) {
//            System.out.println("progress " + i);


            while (!futureList.get(i).isDone()) {

            }
        }
        executor.shutdown();
        System.out.println("finish find KP/Descr");
    }
}
