import java.util.*;

public class Group {

    List<Student> students;
    int groupID;

    int getGroupID(){
        return this.groupID;
    }

    public Group(int groupID){
        this.students = new ArrayList<>();
        this.groupID=groupID;
    }

    public void addStudent(Student student){
        students.add(student);
    }


    public boolean containsStudent(String studentName){
        for(Student s: students){
            if(s.checkIfStudentIsReferenced(studentName)){
                return true;
            }
        }
        return false;
    }

    public void printStudentsInfo(){
        //int i = 0;
        //System.out.println("Printing contents of group "+i);
        Collections.sort(students);
        for(Student s: students){
            s.printStudentFeedback();
        //    i++;
        }
    }

    public String getStudentsInfoCSV(){
        Collections.sort(students);
        String str = "";
        for(Student s: students){
            str=str+s.getStudentFeedbackCSV();
            //    i++;
        }
        return str;
    }


    private List<String> getStudentNamesExcept(String studentName){
        List<String> listOfStudents = new ArrayList<>();
        for(Student s: students){
            if(s.getStudentName().equalsIgnoreCase(studentName)){

            }
            else{
                listOfStudents.add(s.getStudentName());
            }

        }
        return listOfStudents;
    }

    public void validateGroupConsistency() throws GroupErrorException{
        for(Student s: students){
            List<String>  ls1 = getStudentNamesExcept(s.getStudentName());
            List<String> ls2 = s.getFeedbackRecipients();
            for(String s1: ls1){
                if(ls2.contains(s1)==false){
                    String errorMessage = "Student "+s.getStudentName()+"did not provide all feedback\n";
                    errorMessage=errorMessage+"its file source is:"+s.getSourceFile();
                    throw new GroupErrorException(errorMessage);
                }
            }
        }

    }

}
