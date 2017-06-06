package sample;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by vadim on 06.06.2017.
 */
public class MyPreloader extends Preloader {
    private Stage preloaderStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage=primaryStage;

        VBox loading = new VBox();
        Image imagese = new Image("gif.gif");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        ImageView iv1 = new ImageView();
        iv1.setImage(imagese);
        loading.getChildren().add(iv1);



        loading.setMaxWidth(Region.USE_PREF_SIZE);
        loading.setMaxHeight(Region.USE_PREF_SIZE);
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(390);
        loading.getChildren().add(progressBar);
        loading.getChildren().add(new Label("Please wait..."));
        preloaderStage.setScene(new Scene(loading,390,250));
        primaryStage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == StateChangeNotification.Type.BEFORE_START) {
            preloaderStage.hide();
        }
    }
}
