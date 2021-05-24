import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class PeerReview_gson {

    @SerializedName("group")
    private List<Student_gson> students;

    @SerializedName("confidential_comments")
    private String confidentialComments;

    private String source;
    public String getComment() {
        return confidentialComments;
    }
    void setFileName(String fileName){
        this.source=fileName;
    }
    public List<Student_gson> getStudents(){
        return this.students;
    }


    public List<StudentFeedback> extractStudentFeedback() throws RuntimeException{
//        Student_gson.Student student = new Student_gson.Student(students.get(0).getSfuEmail(),confidentialComment,students.size());
//        Stream.Builder<StudentFeedback> fsb = Stream.builder();
        validateFields();

        List<StudentFeedback> output = new ArrayList<StudentFeedback>();
        StudentFeedback feedback;
        int size = students.size();
        for(int i = 0; i<size; i++) {
            if(i==0){
                feedback = new StudentFeedback(students.get(i));
                feedback.setFileSource(source);
                feedback.setFeedbackToInstructor(confidentialComments);
            }
            else{
                feedback = new StudentFeedback(students.get(0).getSfuEmail(),students.get(i));
                feedback.setFileSource(source);
                feedback.setFeedbackToInstructor(confidentialComments);
            }
            output.add(feedback);
        }
        validateData(output);

        return output;
    }

    void validateFields() throws RuntimeException{
        if(students==null){
            System.out.println("students field is null in file+"+source+"\n");
            throw new RuntimeException();
        }
        else if(students.size() == 0){
            System.out.println("students array is empty in file+"+source+"\n");
            throw new RuntimeException();
        }
        else{
            for(Student_gson sg: students){
                if(sg.getContribution()==null){
                    System.out.println("contribution field is missing in file+"+source+"\n");
                    throw new RuntimeException();
                }
            }
        }
    }

    boolean validateData(List<StudentFeedback> sfs) throws RuntimeException{
        double sum = 0;
        double score;
        for(StudentFeedback sf:sfs){
            score = sf.getScore();
            if(score<0){
                System.out.println("feedback score less then zero detected in file:"+source+"\n");
                throw new RuntimeException();
            }
            sum=sum+score;
        }
        if(Math.abs(sum-(20 * sfs.size())) > 0.1){
            double d = Math.abs(sum-(20 * sfs.size()));
            System.out.println("sum is outside tolerance limit"+source+"\n");
            throw new RuntimeException();
        }
        return true;
        };


}
