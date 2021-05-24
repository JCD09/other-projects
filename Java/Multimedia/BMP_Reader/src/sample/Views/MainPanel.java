package sample.Views;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import sample.BMP_File_Processor.BMP_File_Processor;

import java.io.File;

public class MainPanel {

    public static final double SCENE_WIDTH = 800;
    public static final double SCENE_HEIGHT = 1000;


    private FlowPane mainPanel;

    private Pane mainPane;

    private TabPane imageSection;
    private GridPane controlsSection;
    private BMP_File_Processor fileProcessor;

    public MainPanel(){

        configureMainPane();
        configureImagesPane();
        configureControlsSection();

        configureButtons();

        }


    public FlowPane getMainPanel(){return this.mainPanel;}

    private void configureImagesPane(){


        this.imageSection = new TabPane();
        this.imageSection.setPrefWidth(800);
        this.imageSection.setPrefHeight(1000*0.7);
        this.imageSection.setMinWidth(800);

//        this.imageSection.setTabMaxHeight(100);
//        this.imageSection.setTabMaxWidth(SCENE_WIDTH/6);
//        this.imageSection.setPrefSize(SCENE_WIDTH/6,100);
//        this.imageSection.setTabMinHeight(SCENE_HEIGHT);
//        this.imageSection.setTabMaxWidth(SCENE_WIDTH);


        String imagesCSS = "    -fx-border-color:black;\n" +
                "    -fx-border-width: 1;\n" +
                "    -fx-border-insets: 2;\n" +
                "    -fx-background-color: lightgrey;";


        this.imageSection.setStyle(imagesCSS);

        Tab originalImage = new Tab();

        Tab histogram = new Tab();
        histogram.setText("Histograms");

        Tab brightness = new Tab();
        brightness.setText("Brightness");

        Tab greyscale = new Tab();
        greyscale.setText("Grey Scale");

        Tab dither = new Tab();
        dither.setText("Dither Image");

        originalImage.setText("Original Image");

        imageSection.getTabs().add(originalImage);
        imageSection.getTabs().add(histogram);
        imageSection.getTabs().add(brightness);
        imageSection.getTabs().add(greyscale);
        imageSection.getTabs().add(dither);

        mainPanel.getChildren().add(imageSection);
    }


    private void configureMainPane(){
        mainPanel = new FlowPane();
        mainPanel.setMaxWidth(SCENE_WIDTH);
        mainPanel.setMaxHeight(SCENE_HEIGHT);
        mainPanel.setOrientation(Orientation.VERTICAL);
    }

    private void configureControlsSection(){

        String imagesCSS = "    -fx-border-color:black;\n" +
                "    -fx-border-width: 1;\n" +
                "    -fx-border-insets: 2;\n" +
                "    -fx-background-color: lightgrey;";

        controlsSection = new GridPane();
        controlsSection.setAlignment(Pos.CENTER);
        controlsSection.setHgap(10);
        controlsSection.setVgap(12);

        this.controlsSection.setPrefWidth(800);
        this.controlsSection.setPrefHeight(1000*0.3);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.RIGHT);
        controlsSection.getColumnConstraints().add(column1);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHalignment(HPos.LEFT);
        controlsSection.getColumnConstraints().add(column2);

        controlsSection.setStyle(imagesCSS);

        mainPanel.getChildren().add(this.controlsSection);
    }

    public void configureButtons(){

        String exitCSS = "-fx-border-color:black;\n" +
                "-fx-border-width: 1;-fx-font-size:30px;" +
                "-fx-text-align: center;" +
                "-fx-background-color: #4CAF50;" +
                "-fx-padding: 15px 32px;" +
                "-fx-color: white;";


        Button exit = new Button();
        exit.setPrefSize(200,100);
        exit.setOnAction(e->Platform.exit());
        exit.setText("Exit");
        exit.setStyle(exitCSS);

        Button loadFile = new Button();
        loadFile.setPrefSize(300,100);
        loadFile.setOnAction(e->Platform.exit());
        loadFile.setText("Load File");
        loadFile.setStyle(exitCSS);

        loadFile.setOnAction(e->loadAndProcessFile());


        controlsSection.add(exit,1,0);
        controlsSection.add(loadFile,0,0);

    }

    private void addImages(){

    }

    private void loadAndProcessFile(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter =
            new FileChooser.ExtensionFilter("BMP files (*.bmp)", "*.bmp","*.BMP");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(null);

        this.fileProcessor= new BMP_File_Processor(file.getAbsolutePath());
        Canvas image = fileProcessor.getOriginalImage();
        Canvas brightImage = fileProcessor.getBrightImage();
        Canvas greyImage = fileProcessor.getGreyScaleImage();
        TabPane histogram = fileProcessor.getHistograms();
        Canvas ditherImage = fileProcessor.getDitheringImage();


        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(image);
        imageSection.getTabs().get(0).setContent(hb);

        HBox hb4 = new HBox();
        hb4.setAlignment(Pos.CENTER);
        hb4.getChildren().add(histogram);
        imageSection.getTabs().get(1).setContent(hb4);

        HBox hb2 = new HBox();
        hb2.setAlignment(Pos.CENTER);
        hb2.getChildren().add(brightImage);
        imageSection.getTabs().get(2).setContent(hb2);

        HBox hb3 = new HBox();
        hb3.setAlignment(Pos.CENTER);
        hb3.getChildren().add(greyImage);
        imageSection.getTabs().get(3).setContent(hb3);

        HBox hb5 = new HBox();
        hb5.setAlignment(Pos.CENTER);
        hb5.getChildren().add(ditherImage);
        imageSection.getTabs().get(4).setContent(hb5);
    }



}
