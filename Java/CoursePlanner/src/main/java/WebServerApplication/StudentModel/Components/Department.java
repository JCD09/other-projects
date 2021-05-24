package WebServerApplication.StudentModel.Components;

//THIS IS SO FCKIN BAD;
import java.util.*;

public class Department {


    private int deptId;
    private String name;

    private Map<Integer,Course> courses;
    public List<Course> getAllCourses(){return new ArrayList<Course>(courses.values());}

//    public Optional<List<Offering>> getCourseOfferingByCourseID(int courseID){
//        Optional<Course> key = courses.keySet().stream().
//                filter(k-> k.getCourseId()==courseID).
//                findFirst();
//        Optional<List<Offering>> result = Optional.of();
//
//    }

    public Department(int depID, Record record){
        deptId =depID;
        name =record.subjectName;
        courses = new HashMap<>();
    }

    // pattern can be abstracted;
    public void addCourse(Record record ){
        //System.out.println("Calling add Course \n");

        Course course = findCourse(record.catalogNumber).
                orElseGet(()->{
                    Course c =new Course(courses.size(),record);
                    courses.put(courses.size(),c);
                    return c;
                });
        //System.out.println(course+"\n");
        course.addOffering(record);
    }


    private Optional<Course> findCourse(String catalogNumber){
        return courses.values().
                stream().
                filter(course -> course.getCatalogNumber().equals(catalogNumber)).
                map(course->{;
                return course;})
                .findFirst();
    }

    private SimpleDepartment toSimpleDepartment(){
        return new SimpleDepartment(this);
    }

    @Override
    public String toString() {
        return "Department{" +
                "deptId=" + deptId +
                ", name='" + name + '\'' +
                ", courses=" + courses +
                '}';
    }


    private int getSize(){
        if(courses==null) {this.courses=new HashMap();}
        return courses.size();
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimpleDepartment toJson(){
        return new SimpleDepartment(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return deptId == that.deptId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptId, name);
    }

    public boolean check(int id){
        return this.deptId==id;
    }


}
