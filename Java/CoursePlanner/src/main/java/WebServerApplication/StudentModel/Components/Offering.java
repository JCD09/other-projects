package WebServerApplication.StudentModel.Components;

import WebServerApplication.StudentModel.Data.Tuple;

import java.util.*;


// Observable
public class Offering {

    int courseOfferingId;
    SimpleCourse course;
    String location;
    String instructors;
    String semester;
    int semesterCode;
    int year;
    Set<String> setInstructors;

    List<Section> sections;

    public List<Section> getSections(){return this.sections;}


//    public static Offering getCourseOffering(Record record){
//        assert(record!=null);
//        int otherSemesterCode = record.term;
//        String otherLocation = record.location;
//        Optional<Offering> result = courseOfferings.
//                stream().
//                filter(c->{
//                    //System.out.println(c.isEqual(otherLocation,otherSemesterCode));
//                    return c.isEqual(otherLocation,otherSemesterCode);
//                }).findFirst();
//        if(result.isPresent()==false){
//            Offering result2 = new Offering(record);return result2;
//        }
//        else{
//            result.get().setInstructors(result.get().addInstructors(record.instructor));
//            return result.get();}}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Offering)) return false;
        Offering that = (Offering) o;
        return courseOfferingId == that.courseOfferingId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(courseOfferingId);
    }

    public Offering(int courseOfferingId, Record record, SimpleCourse course){

        this.courseOfferingId = courseOfferingId;
        this.semesterCode=record.semester;
        this.location=record.location;
        this.instructors= "";
        this.semester=getSemester(this.semesterCode).first;
        this.year = Integer.parseInt(getSemester(this.semesterCode).second);
        this.course=course;
        this.sections = new ArrayList<>();
    }

    public boolean isEqual(String location,int semesterCode){
        boolean test = (this.location.equals(location))&&(this.semesterCode==semesterCode);
        return test;
    }

    public static Tuple<String,String> getSemester(int code){
        String s = new Integer(code).toString();
        String year = "20"+s.substring(1,3);
        char c = s.charAt(s.length()-1);
        String semester;
        if(c == '1') semester="Spring";
        else if(c == '4') semester="Summer";
        else if(c == '7') semester="Fall";
        else{semester="";}
        return new Tuple<>(semester, year);
    }

    @Override
    public String toString() {
        return "Offering{" +
                "courseOfferingId=" + courseOfferingId +
                ", semesterCode=" + semesterCode +
                ", location='" + location + '\'' +
                ", instructor='" + instructors + '\'' +
                ", term='" + semester + '\'' +
                ", course=" + course +
                ", year='" + year + '\'' +
                '}';
    }

    public String addInstructors(List<String> newInstructors){
        //System.out.println("old instructor +"+this.instructor);
        //System.out.println("new instructor+"+ newInstructors);

        if (this.setInstructors==null){this.setInstructors=new HashSet<>();}
        newInstructors.stream().filter(string->!string.trim().equals("")).
                forEach(instr->this.setInstructors.add(instr));
        //System.out.println("set after addition"+this.setInstructors);
        //System.out.println(this.setInstructors.toString().substring(1,this.setInstructors.toString().length()-1));
        this.instructors=this.setInstructors.toString().substring(1,this.setInstructors.toString().length()-1);
        return this.setInstructors.toString().substring(1,this.setInstructors.toString().length()-1);
    }

    public int getCourseOfferingId() {
        return courseOfferingId;
    }

    public void setCourseOfferingId(int courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(int semesterCode) {
        this.semesterCode = semesterCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInstructors() {
        return instructors;
    }

    public void setInstructors(String instructors) {
        this.instructors = instructors;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void addSection(Section section){
        sections.add(section);
    }

    public SimpleCourse getCourse() {
        return course;
    }

    public void setCourse(SimpleCourse course) {
        this.course = course;
    }

    public SimpleCourseOffering toJson(){
        return new SimpleCourseOffering(this);
    }

}
