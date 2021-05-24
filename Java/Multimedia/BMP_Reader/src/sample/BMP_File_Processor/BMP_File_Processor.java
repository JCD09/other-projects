package sample.BMP_File_Processor;

import com.sun.javafx.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Predicate;


public class BMP_File_Processor {

    Canvas image;
    Canvas brightImage;
    Canvas greyScaleImage;
    Canvas ditherImage;

    TabPane histogram;

    private int[] redCount = new int[256];
    private int[] greenCount = new int[256];
    private int[] blueCount = new int[256];

    public Canvas getOriginalImage(){
        return this.image;
    }

    public TabPane getHistograms(){
        return this.histogram;
    }
//
    public Canvas getBrightImage(){
        return this.brightImage;
    }

//
    public Canvas getGreyScaleImage(){
        return this.greyScaleImage;
    }

//
    public Canvas getDitheringImage(){
        return this.ditherImage;
    };

    public BMP_File_Processor(String path){
        String bmp_file_path = "/Files/download.bmp";
        String bmp_file_path2 = "/Files/FLAG_B24.BMP";

        String currentDir = System.getProperty("user.dir");
        String filePath_Str = currentDir+bmp_file_path;

        Path filePath = Paths.get(path);

        try {

            byte[] data_bmp = Files.readAllBytes(filePath);
            //Header information
            byte[] signature = Arrays.copyOfRange(data_bmp,0,2); // BM 2 Bytes
            byte[] fileSize = reverseArray(Arrays.copyOfRange(data_bmp,2,6)); // # Bytes
            byte[] reserved = Arrays.copyOfRange(data_bmp,6,10);
            byte[] dataOffset = reverseArray(Arrays.copyOfRange(data_bmp,10,14));

            // INFO HEADER
            byte[] size = reverseArray(Arrays.copyOfRange(data_bmp,14,18));
            // assert ByteBuffer.wrap(size).getInt() = 40;
            byte[] width_inPixels = reverseArray(Arrays.copyOfRange(data_bmp,18,22));
            byte[] height_inPixels = reverseArray(Arrays.copyOfRange(data_bmp,22,26));
            byte[] planes = reverseArray(Arrays.copyOfRange(data_bmp,26,28));
            byte[] bits_perPixel =reverseArray(Arrays.copyOfRange(data_bmp,28,30));
            byte[] compression =reverseArray(Arrays.copyOfRange(data_bmp,30,34));
            byte[] imageSize = reverseArray(Arrays.copyOfRange(data_bmp,34,38));
            byte[] XPixels_perMeter = reverseArray(Arrays.copyOfRange(data_bmp,38,42));
            byte[] YPixels_perMeter = reverseArray(Arrays.copyOfRange(data_bmp,42,46));
            byte[] colorsUsed = reverseArray(Arrays.copyOfRange(data_bmp,46,50));
            byte[] importantColors = reverseArray(Arrays.copyOfRange(data_bmp,50,54));

            // COLOR TABLE;

            byte[] pixels_inBytes;
            byte[] rgb_data = new byte[data_bmp.length-ByteBuffer.wrap(dataOffset).getInt()];


            int bytes_perPixel = ByteBuffer.wrap(bits_perPixel).getShort()/8;

            if(bytes_perPixel!=3){
                System.out.println("only 24bit images can be processed at the moment");
                System.exit(-1);
            }

            int yPixels = ByteBuffer.wrap(height_inPixels).getInt();
            int xPixels = ByteBuffer.wrap(width_inPixels).getInt();


            this.image = new Canvas();
            this.image.setWidth(xPixels);
            this.image.setHeight(yPixels);

            this.brightImage = new Canvas();
            this.brightImage.setWidth(xPixels);
            this.brightImage.setHeight(yPixels);

            this.greyScaleImage = new Canvas();
            this.greyScaleImage.setWidth(xPixels);
            this.greyScaleImage.setHeight(yPixels);

            this.ditherImage = new Canvas();
            this.ditherImage.setWidth(xPixels);
            this.ditherImage.setHeight(yPixels);

            PixelWriter pxwrt_image = this.image.getGraphicsContext2D().getPixelWriter();
            PixelWriter pxwrt_brImage = this.brightImage.getGraphicsContext2D().getPixelWriter();
            PixelWriter pxwrt_greyImage = this.greyScaleImage.getGraphicsContext2D().getPixelWriter();
            PixelWriter pxwrt_dither = this.ditherImage.getGraphicsContext2D().getPixelWriter();

            int offset = ByteBuffer.wrap(dataOffset).getInt();

            Color newColor;


            Arrays.fill(redCount,0);
            Arrays.fill(greenCount,0);
            Arrays.fill(blueCount,0);

            int red;
            int green;
            int blue;

            int k = 0;

            int maxRedCount = 0;
            for(int j = yPixels; j>0;j-- ){

                for(int i=0; i < xPixels; i++){

                    pixels_inBytes = Arrays.copyOfRange(data_bmp,offset+k*3,offset+3+k*3);

                    blue = Byte.toUnsignedInt(pixels_inBytes[0]);
                    green = Byte.toUnsignedInt(pixels_inBytes[1]);
                    red = Byte.toUnsignedInt(pixels_inBytes[2]);
                    newColor = Color.rgb(red,green,blue);
                    pxwrt_image.setColor(i,j,newColor);

                    redCount[red]++;
                    greenCount[green]++;
                    blueCount[blue]++;

                    if(maxRedCount<red){maxRedCount=red;}

                    double[] hsb = Utils.RGBtoHSB((double)red/255,(double)green/255,(double)blue/255);
                    double hue = hsb[0];
                    double saturation = hsb[1];
                    double brightness = hsb[2];
                    newColor=Color.hsb(hue,saturation,brightness);
                    newColor = newColor.deriveColor(0,1,1.5,1);
                    pxwrt_brImage.setColor(i,j,newColor);

                    newColor = newColor.grayscale();
                    pxwrt_greyImage.setColor(i,j,newColor);

                    newColor= Color.rgb(red,green,blue).grayscale();

                    int f = getImage(newColor.getRed(),i,j);

                    newColor=Color.rgb(f,f,f);
                    pxwrt_dither.setColor(i,j,newColor);
                    k++;
                }
            }

            this.histogram=createHistogram();


        }
        catch (IOException e){System.out.println(e.getMessage());};
    }

    TabPane createHistogram(){

        TabPane tp = new TabPane();
        tp.setPrefSize(800,600);

        Tab redHistogram = new Tab();
        redHistogram.setText("Red");

        BarChart bc = createHistogram(redCount);
        redHistogram.setContent(bc);
        tp.getTabs().add(redHistogram);

        Tab greenHistogram = new Tab();
        greenHistogram.setText("Green");
        greenHistogram.setContent(createHistogram(greenCount));
        tp.getTabs().add(greenHistogram);

        Tab blueHistogram= new Tab();
        blueHistogram.setText("Blue");
        blueHistogram.setContent(createHistogram(blueCount));
        tp.getTabs().add(blueHistogram);

        return tp;

    }

    int getImage(double greyScale, int row, int column){
        double scalingParamter = (double) 1/17; // 128;
        double newIntensity = greyScale/scalingParamter;
        System.out.printf(greyScale+"\n");
        System.out.println(newIntensity);

        int i = row % 4;
        int j = column % 4;

        double dm1 = 1;

        double[][] ditherMatrix2= {
            {0,2},{3,1}};


        double[][] ditherMatrix = {{0,8,2,10},{12,4,14,6},{3,11,1,9},{15,7,13,5}};
        if(ditherMatrix[j][i] > newIntensity){
            return 0;
        }
        else return 255;

    }

    BarChart createHistogram(int[] colorCh){

        CategoryAxis xAxis = new CategoryAxis();
        ObservableList<String> ol = FXCollections.observableArrayList();

        for(int i = 0; i < 256; i++){
            ol.add(i+"");
        }
        xAxis.setLabel("Channel Number");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setMinorTickVisible(true);

        BarChart histogram = new BarChart<>(xAxis, yAxis);
        histogram.setPrefSize(800,600);
        histogram.setCategoryGap(1);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();

        for(int i = 0; i < redCount.length;i++){
            series1.getData().add(new XYChart.Data<>(i+"", colorCh[i]));
        }

        histogram.getData().add(series1);
        return histogram;
    }



    private static byte[] reverseArray(byte[] array){
        int size = array.length;
        byte[] reversed = new byte[size];
        for(int i = 0; i < size; i++){
            reversed[size-1-i]=array[i];
        }
        return reversed;
    }
}
