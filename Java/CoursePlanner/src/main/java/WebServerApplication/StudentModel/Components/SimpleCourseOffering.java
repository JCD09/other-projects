package WebServerApplication.StudentModel.Components;

public class SimpleCourseOffering {

    int courseOfferingId;
    SimpleCourse course;
    String location;
    String instructors;
    String term;
    int semesterCode;
    int year;

    public int getCourseOfferingId() {
        return courseOfferingId;
    }

    public void setCourseOfferingId(int courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
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

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public SimpleCourse getCourse() {
        return course;
    }

    public void setCourse(SimpleCourse course) {
        this.course = course;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "SimpleCourseOffering{" +
                "courseOfferingId=" + courseOfferingId +
                ", course=" + course +
                ", location='" + location + '\'' +
                ", instructor='" + instructors + '\'' +
                ", term='" + term + '\'' +
                ", semesterCode=" + semesterCode +
                ", year=" + year +
                '}';
    }

    public SimpleCourseOffering(Offering co){
        this.courseOfferingId =co.getCourseOfferingId();
        this.semesterCode=co.getSemesterCode();
        this.location=co.getLocation();
        this.instructors=co.instructors;
        this.term =co.semester;
        this.course=co.course;
        this.year=co.year;
    }

}
