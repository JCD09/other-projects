package sample.Spectrogram;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class SView {

    private Color fillColor = Color.LIGHTGREY; // Border
    private StrokeType strokeType = StrokeType.CENTERED;


    private double panelHeight;

    private Group canvas;

    private Rectangle rectangle;

    private SModel model;

    double startXPosition;

    double centerX;

    private ScrollPane sp;

    private double LINE_WIDTH = 0.1;

    public ScrollPane getScrollPane(){
        return this.sp;
    }

    public SView(double scrollPanelWidth, double scrollPanelHeight, String path){
        panelHeight=scrollPanelHeight;
        // create a model;
        this.model = new SModel(path);
        // number of amplitudes;
        double rectangleWidth = model.getAmplitudes().length;

        this.canvas = new Group();

        rectangle = new Rectangle(0,0,rectangleWidth*LINE_WIDTH,scrollPanelHeight);
        rectangle.setFill(Color.LIGHTGREY);
        rectangle.setStrokeType(strokeType);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(LINE_WIDTH);

        canvas.getChildren().add(rectangle);
        startXPosition = 1;
        centerX = scrollPanelHeight/2;

        addLines(model.getAmplitudes());

        this.sp = new ScrollPane();
        sp.setPrefSize(scrollPanelWidth, scrollPanelHeight);
        sp.setContent(canvas);

    }

    public void addLines(short amplitudes[]){
        int size = amplitudes.length;
        for(int i = 0; i < size; i ++){
            canvas.getChildren().add(createLine(amplitudes[i]));
        }
    }

    private Line createLine(short amplitude){
        double maxValue = 32767;
        double scaledAmplitude = (panelHeight*0.6/2)*(amplitude/maxValue);
        Line line;

        if(scaledAmplitude<0){
            line = new Line(startXPosition,centerX,startXPosition,centerX+scaledAmplitude);
        }
        else {
            line = new Line(startXPosition,centerX+scaledAmplitude,startXPosition,centerX);
        }

        line.setStroke(Color.BLACK);
        line.setStrokeWidth(LINE_WIDTH);
        line.setStrokeLineCap(StrokeLineCap.BUTT);
        startXPosition=startXPosition+LINE_WIDTH;
        return line;
    }

    public SModel getSModel(){return this.model;}

    public void addLine(double lineHeight){
    }
}
