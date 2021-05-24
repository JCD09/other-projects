import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackAggregator {
    private  List<Group> groups;
    private  Map<String, Student> students;

    FeedbackAggregator(){
        this.groups = new ArrayList<>();
        this.students = new HashMap<>();
    }

    // Student S belongs to group G; if a group G contains another student R such that R provided feedback to S !!!!!!!
    //

    // find group by student name
    public Group getGroup(String studentName){
        for(Group g: groups){
            if(g.containsStudent(studentName)){
                //System.out.println("returning group G");
                return g;
            }
        }
        return new Group(groups.size());
    }

    // This should be improved;
    public boolean contains(Group gr){
        int grID = gr.getGroupID();
        for(Group g:groups){
            if(grID==g.getGroupID()){return true;}
        }
        return false;
    }


    public void addStudentsIntoGroups(){
        students.forEach((name,Student)->{
            //System.out.printf("we are here  \n");
            //String studentName=Student.getStudentName();
            Group g = getGroup(name);
            g.addStudent(Student);
            if(contains(g)){}
            else{groups.add(g);}
        });
    }

    public void printGroupData(){
        int i = 1;
        System.out.println("Group#,Source Student,Target Student,Score,Comment,,Private");
        for(Group g: groups){
            System.out.printf("Group:"+i+"\n");
            g.printStudentsInfo();
            i++;
        }
    }

    public String getCSVString(){
        int i = 1;
        String csv = "Group#,Source Student,Target Student,Score,Comment,,Private\n";

        for(Group g: groups){
            if(i > 1){
                csv=csv+"\nGroup "+i+"\n";
                csv=csv+g.getStudentsInfoCSV();
                i++;
            }
            else{
                csv=csv+"Group "+i+"\n";
                csv=csv+g.getStudentsInfoCSV();
                i++;
            }

        }
        return csv;
    }


    public void insertStudent (StudentFeedback student) throws GroupErrorException {
        String studentName = student.getFeedbackSender();
        if(students.containsKey(studentName)){
            String error = "Dublicate JSON File detected:"+students.get(studentName).getSourceFile()+"and"+
                    student.getFileSource();
            throw new GroupErrorException(error);
        }
        else {
            Student newStudent = new Student(student);
            students.put(studentName,newStudent);
        }
        return ;
    }

    public void addStudents(List<StudentFeedback> studentFeedback) throws GroupErrorException{
        for(StudentFeedback sf:studentFeedback){
            insertStudent(sf);
        }
    }


    public void incorporateFeedback(List<StudentFeedback> studentFeedback) throws GroupErrorException{
        //System.out.printf(feedback.toString());
        if(studentFeedback!=null){
            for(StudentFeedback sf:studentFeedback){
                String senderName = sf.getFeedbackSender();
                String recipientName = sf.getFeedbackRecipient();

                if(students.containsKey(recipientName)==false){
                    String error = "Student: "+recipientName+" did not submit JSON file or name is incorrectly written \n"+
                            "Source File:"+sf.getFileSource();
                    throw new GroupErrorException(error);
                }

                students.get(senderName).addFeedbackRecipient(recipientName);
                students.get(recipientName).addFeedback(sf);
            }
        }
    }

    public void validateGroups() throws GroupErrorException {
        try{
            for(Group g: groups){
                g.validateGroupConsistency();
            }
        }
        catch(GroupErrorException g){
            throw new GroupErrorException(g.getMessage());
        }
    }

    // last moment attempt;
    // checks if student is in the same group;
    public void validateStudentsAssignment() throws GroupErrorException {
        int count;
        String studentName;

            for(Student s: students.values()){
                count = 0;
                studentName=s.getStudentName();

                for(Group g: groups){
                    if(g.containsStudent(studentName));{
                        count++;
                        break;
                    }
                }
                if(count > 1){

                    throw new GroupErrorException("Student is referenced by students in two different groups");
                }

            }
    }
}
