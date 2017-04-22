package scienceWork.ObjectClasses;


import javafx.collections.ObservableList;
import org.opencv.core.Core;
import scienceWork.Interfaces.IAlgorithm;
import scienceWork.WorkFolder;
import scienceWork.algorithms.Descriptor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Algorithms implements IAlgorithm {
    private static Algorithms algorithms;
    private DescriptorProperty futuresDescriptor;
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    @Override
    public double getValue() {
        return 0;
    }

    public static Algorithms getInstance() {
        if (algorithms != null) return algorithms;
        else {
            algorithms = new Algorithms();
            return algorithms;
        }
    }

    public void startMethodDistanceKP(ObservableList<Picture> pictList) {

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future> futureList=new LinkedList<>();
        for (Picture picture : pictList) {
            Descriptor descriptor = new Descriptor(picture);
            System.out.println("photo:"+picture.getName());
            try {
               futureList.add(executor.submit(descriptor));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0;i<pictList.size();i++){
            while(!futureList.get(i).isDone());
            System.out.println(i+" - "+futureList.get(i).isDone());
        }
        for (Picture picture : pictList) {
         picture.getValueAlgorithm().setNormKeyPoints(picture.getValueAlgorithm().getNormKeyPoints()* WorkFolder.avrgH/WorkFolder.count*WorkFolder.avrgW/WorkFolder.count);
        }
        System.out.println("H="+WorkFolder.avrgH/WorkFolder.count+"\n"+"W="+WorkFolder.avrgW/WorkFolder.count+"\n"+WorkFolder.count);
//        for (Picture picture : pictList) {
//            Descriptor descriptor = new Descriptor(picture);
//            descriptor.run();
//            System.out.println("photo:"+picture.getName());
//        }
    }
}
