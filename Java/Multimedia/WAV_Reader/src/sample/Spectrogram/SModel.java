package sample.Spectrogram;


import com.sun.tools.javac.util.List;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class SModel {


    private short[] amplitudes;
    private int numberOfSamples;
    private int maxAmp;

    public short[] getAmplitudes(){
        return amplitudes;
    }

    public SModel(String path){

        Path filePath = Paths.get(path);
        this.amplitudes = new short[0];

        try {
            byte[] samples = Files.readAllBytes(filePath);

            byte[] format = Arrays.copyOfRange(samples,8,12);
            byte[] chunkID = Arrays.copyOfRange(samples,0,4);
            byte[] chunkSize = reverseArray(Arrays.copyOfRange(samples,4,8));

            byte[] subChunkOneID = Arrays.copyOfRange(samples,12,16);
            byte[] subChunkOneSize = reverseArray(Arrays.copyOfRange(samples,16,20));
            byte[] audioFormat = reverseArray(Arrays.copyOfRange(samples,20,22));
            byte[] numberOfChannels = reverseArray(Arrays.copyOfRange(samples,22,24));
            byte[] sampleRate = reverseArray(Arrays.copyOfRange(samples,24,28));
            byte[] byteRate = reverseArray(Arrays.copyOfRange(samples,28,32));
            byte[] blockAlign = reverseArray(Arrays.copyOfRange(samples,32,34));
            byte[] bitsPerSample = reverseArray(Arrays.copyOfRange(samples,34,36));

            byte[] subChunkTwoID = Arrays.copyOfRange(samples,36,40);
            byte[] subChunkTwoSize = reverseArray(Arrays.copyOfRange(samples,40,44));

            this.numberOfSamples = samples.length-43;

            short extractedAmplitude;

            this.amplitudes=new short[numberOfSamples];
            this.maxAmp = -32768;

            for(int i = 0; i < numberOfSamples; i=i+2) {
                extractedAmplitude = ByteBuffer.wrap(reverseArray(Arrays.copyOfRange(samples,44+i,46+i))).getShort();
                amplitudes[i]=extractedAmplitude;
                if(this.maxAmp<extractedAmplitude){
                    this.maxAmp=extractedAmplitude;
                }
            }
        }
        catch(IOException e){
            System.out.println("error Occured");
        }
    }

    public int getMaxAmp(){return this.maxAmp;}

    public static void main(String args[])
    {}

    private static byte[] reverseArray(byte[] array){
        int size = array.length;
        byte[] reversed = new byte[size];
        for(int i = 0; i < size; i++){
            reversed[size-1-i]=array[i];
        }
        return reversed;
    }


}
