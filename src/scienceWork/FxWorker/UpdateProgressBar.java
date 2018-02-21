package scienceWork.FxWorker;

import javafx.scene.control.ProgressBar;
import scienceWork.FxWorker.Interfaces.Progress;

public class UpdateProgressBar implements Progress{

    private ProgressBar progressBar;

    public UpdateProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
//        FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/mainView.fxml"));
//        try {
//            loader.load();
//        } catch (Exception e) {
//            System.out.println("UpdateProgressBar Error");
//            e.printStackTrace();
//        }

    }

    public void setProgress(long current, long total){
        progressBar.setProgress((double)current/total);
           }

}
