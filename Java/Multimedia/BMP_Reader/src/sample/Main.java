package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import sample.BMP_File_Processor.BMP_File_Processor;
import sample.Views.MainPanel;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        double SCENE_WIDTH = 800;
        double SCENE_HEIGHT = 1000;

        MainPanel m = new MainPanel();

        Scene scene = new Scene(m.getMainPanel(),SCENE_WIDTH,SCENE_HEIGHT);
        scene.getStylesheets().add("/resources/css/root.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
