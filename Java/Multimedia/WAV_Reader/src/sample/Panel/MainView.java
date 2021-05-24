package sample.Panel;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import sample.Spectrogram.SView;

import java.io.File;

public class MainView {

    private SView spectrogramView;
    private FlowPane root;
    private GridPane controls;

    private double width;
    private double height;

    private IntegerProperty numberOfSamples = new SimpleIntegerProperty(0);
    private IntegerProperty maximumAmplitude= new SimpleIntegerProperty(0);

    private HBox hbox;



    public MainView(double panelWidth, double panelHeight){
        this.width=panelWidth;
        this.height=panelHeight;

        this.root= new FlowPane();
        root.setOrientation(Orientation.VERTICAL);

        this.hbox = new HBox();
        hbox.setPrefSize(panelWidth,panelHeight);

        configureGridPane(panelWidth,panelHeight);
        addLabels();
        addButtons();

        hbox.getChildren().add(controls);

        root.getChildren().add(hbox);
        root.getChildren().add(1,controls);



    }

    private void addButtons(){

        Button loadFile = new Button("Load File");
        controls.add(loadFile,1,3);
        loadFile.setOnAction((e)->{
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter =
                    new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showOpenDialog(null);

            spectrogramView = new SView(width,height,file.getAbsolutePath());
            hbox.getChildren().add(0,spectrogramView.getScrollPane());
            this.numberOfSamples.set(spectrogramView.getSModel().getAmplitudes().length);
            this.maximumAmplitude.set(spectrogramView.getSModel().getMaxAmp());
        });

        Button exit = new Button("Exit");
        controls.add(exit,2,3);
        exit.setOnAction(e -> Platform.exit());


    }

    private void addLabels(){

        Label numOfSamples = new Label("Number of Samples:");
        numOfSamples.setStyle("-fx-border-color: blue;");
        numOfSamples.setFont(new Font(14));
        controls.add(numOfSamples,1,1);

        Label value = new Label("");
        //value.setStyle("-fx-border-color: black;");
        value.setFont(new Font(14));
        value.textProperty().bind(numberOfSamples.asString());
        controls.add(value,2,1);

        Label maxAmplitude = new Label("Maximum Amplitude");
        maxAmplitude.setStyle("-fx-border-color: black;");
        maxAmplitude.setFont(new Font(14));
        controls.add(maxAmplitude,1,2);

        Label maxValue = new Label("");
        //maxValue.setStyle("-fx-border-color: black;");
        maxValue.setFont(new Font(14));
        maxValue.textProperty().bind(maximumAmplitude.asString());
        controls.add(maxValue,2,2);

    }

    private void configureGridPane(double width, double height){
        this.controls = new GridPane();
        this.controls.setPrefSize(width*0.4,height);
        controls.setHgap(10);
        controls.setVgap(20);
    }

    public FlowPane getRoot(){
        return this.root;
    }


}
