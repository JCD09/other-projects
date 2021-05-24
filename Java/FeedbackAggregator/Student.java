import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student implements Comparable<Student> {

    private String name;
    // list of collected feed to this student;
    private List<StudentFeedback> feedback;

    private String sourceFile;
    private String instructorComment;

    private double selfScore;
    private String selfComments;

    private List<String> feedbackRecipients;

    @Override
    public int compareTo(Student student) {
        return this.name.compareTo(student.getStudentName());
    }

    public Student(StudentFeedback feedback){
        this.sourceFile = feedback.getFileSource();
        this.instructorComment=feedback.getPrivateFeedback();
        this.name = feedback.getFeedbackSender();
        this.feedback=new ArrayList<>();
        this.selfScore=feedback.getScore();
        this.selfComments=feedback.getFeedback();
        this.sourceFile=feedback.getFileSource();
        this.instructorComment=feedback.getPrivateFeedback();
        this.feedbackRecipients = new ArrayList<>();
    }

    public void addFeedbackRecipient(String fr){
        feedbackRecipients.add(fr);
    }

    public boolean checkIfStudentIsReferenced(String studentName){
        for(String s: feedbackRecipients){
            if(s.equalsIgnoreCase(studentName)){return true;}
        }
        return false;
    }

    public String getStudentName(){
        return this.name;
    }

    public void addFeedback(StudentFeedback feedback){
        this.feedback.add(feedback);
    }

    public String getSourceFile(){
        return this.sourceFile;
    }

    public List<String> getFeedbackRecipients(){
        return this.feedbackRecipients;
    }

    public double getAverageScore(){
        double average = 0;
        for(StudentFeedback sf:this.feedback){
            average=average+sf.getScore();
        }
        double d = average/this.feedback.size();
        d = Math.round(d*10);
        d = d/10;
        return d;
    }


    public void printStudentFeedback(){
        Collections.sort(this.feedback);

        for(StudentFeedback sf: feedback){
            System.out.println(sf.toString());
        }
        System.out.println(",-->,"+name+","+selfScore+","+selfComments);
        DecimalFormat df = new DecimalFormat("#.#");
        System.out.println(",-->,"+name+",avg "+df.format(getAverageScore())+","+selfComments);
    }

    public String printAverageScore(){
        DecimalFormat df = new DecimalFormat("#.#");
        if(getAverageScore()==getAverageScore()){
            return df.format(getAverageScore());
        }
        else{
            return "NaN";
        }
    }
        
    public String getStudentFeedbackCSV(){
        Collections.sort(this.feedback);
        String s="";
        for(StudentFeedback sf: feedback){
            s=s+sf.toString();
        }
        s=s+",-->,"+name+","+selfScore+",\""+selfComments.replaceAll("\"","'")+"\"\n";
        //DecimalFormat df = new DecimalFormat("#.#");

        s=s+",-->,"+name+",avg "+printAverageScore()+" /"+feedback.size()+",,,\""+instructorComment.replaceAll("\"","'")+"\"\n";
        return s;
    }
}
