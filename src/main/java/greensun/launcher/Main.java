package greensun.launcher;

import greensun.engine_support.Depacker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by User on 11.06.2017.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Depacker.getStartedConnection(getClass());

        Parent root = FXMLLoader.load(getClass().getResource("/layouts/main_layout.fxml"));
        primaryStage.setResizable(false);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        //primaryStage.setFullScreen(true);
        //primaryStage.setAlwaysOnTop(true);
        //primaryStage.setMaximized(true);
        primaryStage.setTitle("Ultimate Platformer");
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}