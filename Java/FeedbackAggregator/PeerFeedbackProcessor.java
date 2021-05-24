import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.file.*;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

// References: I used two books. Java SE 9 for the impatient, to understand how to work with Streams;
//             and Java 8 in Action  in action to figure out how to aggregate stream elements into maps;
//             Also used online gson tutorials to understand how json processing works;
//             And used SO how to write string into file in java; lost the link;
//
//

public class PeerFeedbackProcessor {
    private Gson gson;

    public enum StudentFeedbackType { SELF, OTHER }

    public PeerFeedbackProcessor(){
        gson = new Gson();
    }

    public boolean isJson(Path path){
        return path.getFileName().toString().toLowerCase().endsWith("json");
    }

    public void validatePath(Path json, Path csv) throws InvalidPathException{
        if(Files.isDirectory(json)==false){

            throw new InvalidPathException("is invalid path json folder",json.toString());
        }
        else if(Files.isDirectory(csv)==false) {
            throw new InvalidPathException("is invalid path csv folder",csv.toString());
        }
        else{
            System.out.println("paths are valid");
        }

    }

    public StudentFeedbackType groupStudentFeedback(StudentFeedback sf){
        {
            if(sf.getFeedbackSender()==sf.getFeedbackRecipient()){
                return StudentFeedbackType.SELF;
            }
            else {
                return StudentFeedbackType.OTHER;
            }
        }

    }

    public PeerReview_gson extractJsonData(Path path){
            try{
                String jsonContents = new String(Files.readAllBytes(path));
                PeerReview_gson peerFeedback = this.gson.fromJson(jsonContents,PeerReview_gson.class);
                peerFeedback.setFileName(path.toString());

                return peerFeedback;
            }
            catch(IOException e){
                System.out.printf("Error occured when opening of the file: "+path.getFileName()+"\n");
                throw new RuntimeException();
            }
            catch( JsonSyntaxException e){
                System.out.printf("error occured  when processing json contents: "+path.getFileName()+"\n");
                throw new RuntimeException();
            }
    }

    public static void main(String[] args){
        PeerFeedbackProcessor pfp = new PeerFeedbackProcessor();

        if(args.length==2)
        { }
        else{
            System.out.println("Incorrect Number of arguments\nExiting");
            System.exit(-1);
        }

        Path JsonPath = Paths.get(args[0]);
        Path CsvPath = Paths.get(args[1]);
        Path csvFileName = Paths.get("groups.csv");
        FeedbackAggregator feedbackAggregator = new FeedbackAggregator();

        try
        {
            pfp.validatePath(JsonPath, CsvPath);

            try(Stream<Path> contents = Files.walk(Paths.get(JsonPath.toString()))){
                try {
                    Map<StudentFeedbackType,List<StudentFeedback>> output =
                            contents.
                                    filter(path -> Files.isRegularFile(path)).
                                    filter(path -> pfp.isJson(path)).
                                    map(path -> pfp.extractJsonData(path)).
                                    flatMap(peerReview_gson->peerReview_gson.extractStudentFeedback().stream()).
                                    collect(Collectors.groupingBy(
                                            student_feedback->pfp.groupStudentFeedback(student_feedback))
                                    );

                    feedbackAggregator.addStudents(output.get(StudentFeedbackType.SELF));
                    feedbackAggregator.incorporateFeedback(output.get(StudentFeedbackType.OTHER));

                    // group students;
                    feedbackAggregator.addStudentsIntoGroups();
                    feedbackAggregator.validateStudentsAssignment();
                    feedbackAggregator.validateGroups();

                    String csvString = feedbackAggregator.getCSVString();

                    BufferedWriter writer = Files.newBufferedWriter(CsvPath.resolve(csvFileName));
                    writer.write(csvString);
                    writer.close();


                } catch(RuntimeException e){
                    System.out.println("Error occured inside Stream\n");
                    System.out.println("exit");
                    System.exit(-1);
                }
                catch(GroupErrorException e){
                    System.out.println(e.getMessage());
                }
            }
            catch(IOException e){
                System.out.printf("Could not find/open directory: "+ JsonPath.toString());
            }
        }
        catch(InvalidPathException e){
            System.out.println(e.getMessage());
            System.out.println("exit");
        }

    }
}
