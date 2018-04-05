package scienceWork.FxWorker;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import scienceWork.FxWorker.Interfaces.Progress;

public class ProgressImp implements Progress {

    private TextArea textArea;
    private ProgressBar progressBar;

    public ProgressImp(ProgressBar progressBar, TextArea textArea) {
        this.progressBar = progressBar;
        this.textArea = textArea;
//        FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/mainView.fxml"));
//        try {
//            loader.load();
//        } catch (Exception e) {
//            System.out.println("ProgressImp Error");
//            e.printStackTrace();
//        }

    }

    @Override
    public void addMessage(String message) {
        textArea.setText(message + "\n" + textArea.getText());
    }

    @Override
    public void setProgress(long current, long total) {
        progressBar.setProgress((double) current / total);
    }

}
