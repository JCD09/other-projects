package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Panel.MainView;

public class Main extends Application {

    // Unrefined version with basic functionality;
    // Hard part of getting started with JavaFX;

    // Definitely needs refactoring;

    private static Stage primaryStage;

    public static Stage getPrimaryStage(){
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception{
        MainView pView = new MainView(800,600);
        primaryStage=stage;

        Scene scene = new Scene(pView.getRoot());

        primaryStage.setTitle("Wav Histogram");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
