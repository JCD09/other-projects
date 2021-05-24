public class StudentFeedback implements Comparable<StudentFeedback> {

    private String from;

    private String to;

    private String feedback;

    private double score;

    private String fileSource;

    private String privateFeedback;

    public int compareTo(StudentFeedback sf){
        return from.compareTo(sf.getFeedbackSender());
    }

    void setFeedbackToInstructor(String privateFeedback){
        this.privateFeedback=privateFeedback;
    }
    void setFileSource(String fileSource){
        this.fileSource=fileSource;
    }

    String getFileSource(){
        return this.fileSource;
    }

    String getPrivateFeedback(){
        return this.privateFeedback;
    }
    String getFeedbackSender(){
        return this.from;
    }

    String getFeedbackRecipient(){
        return this.to;
    }

    double getScore(){
        return this.score;
    }

    String getFeedback(){
        return this.feedback;
    }



    public StudentFeedback(Student_gson student){
        this.from = student.getSfuEmail().trim().toLowerCase();
        this.to = student.getSfuEmail().trim().toLowerCase();
        this.feedback = student.getContribution().getComment();
        this.score = student.getContribution().getScore();
    }

    public StudentFeedback(String from, Student_gson to){
        this.from = from.trim().toLowerCase();
        this.to = to.getSfuEmail().trim().toLowerCase();
        this.feedback = to.getContribution().getComment();
        this.score = to.getContribution().getScore();
    }

    @Override
    public String toString(){
        return ","+from+","+to+","+score+","+"\""+feedback.replaceAll("\"","'")+"\""+",,"+"\n";
    }


}
