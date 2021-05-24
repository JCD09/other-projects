package WebServerApplication.StudentModel.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// This looks ugly. I dont wanna do anything
public class Record {



    public final int semester;

    public final String subjectName;
    public final String catalogNumber;

    public final String location;
    public final int enrollmentCap;
    public final int enrollmentTotal;

    public final List<String> instructors;
    public final String component;

//    public String getInstructor(){
//        StringBuilder instructorss= new StringBuilder("");
//        for(String instructor:this.instructor){
//            if(instructor.indexOf(instructor)==this.instructor.size()-1){
//                instructorss.append(instructor);
//            }
//            else{
//                instructorss.append(instructor+", ");
//            }
//        }
//        return instructorss.toString();
//    }


    public String getSubjectName(){return this.subjectName;}
    private Record(
            int semester,
            String subject,
            String catalogueNumber,
            String location,
            int enrollmentCapacity,
            int enrollmentTotal,
            List<String> instructors,
            String componentCode){
        this.semester=semester;
        this.subjectName =subject;
        this.catalogNumber =catalogueNumber;
        this.location=location;
        this.enrollmentCap =enrollmentCapacity;
        this.enrollmentTotal=enrollmentTotal;
        this.instructors=instructors;
        this.component =componentCode;
    }


    public static class NestedRecordBuilder{
        private int nestedSemester;
        private String nestedsSubject;
        private String nestedCatalogueNumber;
        private String nestedLocation;
        private int nestedeEnrollmentCapacity;
        private int nestedEnrollmentTotal;
        private  List<String> nestedInstructors;
        private  String nestedComponentCode;


        public NestedRecordBuilder(){

        }
        public NestedRecordBuilder addSemesterCode(int semester){
            this.nestedSemester=semester;
            return this;
        }
        public NestedRecordBuilder addSubject(String subject){
            this.nestedsSubject=subject;
            return this;
        }
        public NestedRecordBuilder addCatalogueNumber(String nestedCatalogueNumber){
            this.nestedCatalogueNumber=nestedCatalogueNumber;
            return this;
        }
        public NestedRecordBuilder addLocation(String location){
            this.nestedLocation=location;
            return this;
        }
        public NestedRecordBuilder addEnrollment(int enrollmentCapacity,int enrollmentTotal){
            this.nestedeEnrollmentCapacity=enrollmentCapacity;
            this.nestedEnrollmentTotal=enrollmentTotal;
            return this;
        }
        public NestedRecordBuilder addInstructors(List<String> instructors){
            List<String> newInstructors = new ArrayList<>();
            Pattern patternNull = Pattern.compile("\\<null\\>");
            Matcher match;
            for(String i:instructors){
                match = patternNull.matcher(i);
                if(!match.find()){
                    newInstructors.add(i.trim());
                }
                else{newInstructors.add("");}
            }
            //for(String i:instructor){newInstructors.add(i.trim());}
            if(newInstructors.size()==0){newInstructors.add("");}
            this.nestedInstructors=newInstructors;
            return this;
        }
        public NestedRecordBuilder addComponentCode(String componentCode){
            this.nestedComponentCode=componentCode;
            return this;
        }
        public Record createNewRecord(){
            return new Record(
                    nestedSemester,nestedsSubject,nestedCatalogueNumber,nestedLocation,
                    nestedeEnrollmentCapacity,nestedEnrollmentTotal,
                    nestedInstructors,nestedComponentCode);
        }

    }
    @Override
    public String toString(){
        return "Semester: "+this.semester+ " Subject: "+this.subjectName +
                " Catalogue Number: "+this.catalogNumber +" Location: "+this.location+
                " Enrollment Capacity: "+this.enrollmentCap +" EnrollmentTotal: "+
                this.enrollmentTotal+"\n";
//        return "Component: "+ this.component;
    }

}
