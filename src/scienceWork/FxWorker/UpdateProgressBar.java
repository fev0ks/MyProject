package scienceWork.FxWorker;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressBar;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.view.MainView;

import java.io.IOException;

public class UpdateProgressBar implements Progress{
    private static Progress updateProgressBar;
    private ProgressBar progressBar;
    public static Progress getInstance(){
        if(updateProgressBar ==null){
            updateProgressBar = new UpdateProgressBar();
        }
        return updateProgressBar;
    }

    private UpdateProgressBar() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scienceWork/view/mainView.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        progressBar = loader.<MainView>getController().getProgressBar();
    }

    public void setProgress(long current, long total){
        progressBar.setProgress((total-current)/total);

    }

}
