package scienceWork.FxWorker.Interfaces;

/**
 * Created by mixa1 on 21.02.2018.
 */
public interface Progress {

     void setProgress(long current, long total);
     void addMessage(String message);
}
