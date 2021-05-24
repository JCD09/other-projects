package WebServerApplication.StudentModel;


import WebServerApplication.StudentModel.Data.Validator;
import WebServerApplication.StudentModel.Components.Record;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

// Observer -> Subscriber
// Observable -> Flux;


// declare this class as bean and singleton;!!!!
public class FileReader {
    private static final String FILE_PATH = "Data/course_data_2018.csv"; //
    //private static final String FILE_PATH = "Data/testData1.csv"; //
    // file locations;
    private File userDirectory, file;
    private CharsetMatch encoding;

    public Flux<Record> records;

    FileReader() {
        System.out.println("FILEREADER CREATED PROCESSING DATA\n");
        this.userDirectory = new File(System.getProperty("user.dir"));  //used SO to get these two records
        this.file = new File(userDirectory, FILE_PATH);
        this.encoding=detectEncoding(file);
        if(encoding==null){ System.exit(-1);}
        String fileName = file.getAbsoluteFile().getAbsolutePath();

        //records = Flux.using(() -> Files.records(Paths.get(fileName)), Flux::fromStream, BaseStream::close);
        records =Flux.create(
                fluxSink->{
                    // On nest sent recors to subscriber;
                    try{Files.lines(Paths.get(fileName),Charset.forName(encoding.getName())).skip(1)
                            .map(line->Validator.getRecord(line))
                            .map(record->fluxSink.next(record)).collect(Collectors.toList());
                    }
                    // sent error message;
                    catch (IOException e){
                        fluxSink.error(e); }
                    });
    }

    // just below average code
    private CharsetMatch detectEncoding(File fileName){
        CharsetMatch cm=null;
        try (InputStream bf = new BufferedInputStream(new FileInputStream(fileName))) {
            CharsetDetector cd = new CharsetDetector();
            cm = cd.setText(bf).detect();
            bf.close();
        }
        catch(IOException e){return cm;}
        return cm;
    }

}