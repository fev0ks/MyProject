package scienceWork.Tests;

import scienceWork.Workers.FileWorker;
import scienceWork.objects.Picture;

import java.io.File;
import java.util.List;

/**
 * Created by mixa1 on 25.02.2018.
 */
public class ConsoleMain {

    public static void main(String[] args) {
        List<List<Picture>> pictureList = FileWorker.getInstance().loadPicFromDir(new File("E:\\aфотоТест — копия\\SimpleSet"));
    }
}
