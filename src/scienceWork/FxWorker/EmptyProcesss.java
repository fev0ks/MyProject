package scienceWork.FxWorker;

import scienceWork.FxWorker.Interfaces.Progress;

/**
 * Created by mixa1 on 25.02.2018.
 */
public class EmptyProcesss implements Progress {
    @Override
    public void setProgress(long current, long total) {

    }
    @Override
    public void addMessage(String message) {

    }
}
