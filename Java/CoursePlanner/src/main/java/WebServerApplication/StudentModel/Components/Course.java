package WebServerApplication.StudentModel.Components;

import org.reactivestreams.Subscription;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;


// Watchable -- Observer

public class Course implements Watchable<Course,String> {



    private int courseId; // this is just a number
    private String catalogNumber; // this is string corresponding to course IE 156;

    private Map<Integer,Offering> offerings;
    public List<Offering> getAllOfferings(){return new ArrayList<>(offerings.values());}

    private List<Watcher> watchers;


    public void notifyWatchers(String event){
        watchers.stream().forEach(watcher->watcher.addEvent(event));
    }

    public void addWatcher(Watcher w){watchers.add(w);}

    public Course(int courseId,Record record){
        this.courseId = courseId;
        this.catalogNumber=record.catalogNumber;
        this.offerings = new HashMap<>();
        this.watchers = new ArrayList<>();


    }

    private String generateMessage(Record record){
        //Sun Mar 25 21:41:35 PDT 2018: Added section LEC with enrollment (89 / 90)
        //to offering Spring 2019

        LocalDateTime localDateTime = LocalDateTime.now();

        String addedSection = localDateTime+": Added section "+record.component+"with enrollment ("+record.enrollmentTotal+
                "/" +record.enrollmentCap;
        return addedSection;
    }

    public void addOffering(Record record){


        Offering offering = findOffering(record.location,record.semester).
                orElseGet(()->{
                    Offering c =new Offering(offerings.size(),record,new SimpleCourse(this));
                    //System.out.print(c.getCourse()+"\n");
                    offerings.put(offerings.size(),c);
                    return c;
                });
        Section newSection = new Section(record);
        offering.addSection(newSection);
        offering.addInstructors(record.instructors);
        String message = generateMessage(record);

        notifyWatchers(message);

    }

    private Optional<Offering> findOffering(String location,int semesterCode){
        return offerings.values().
                stream().
                filter(off ->{//System.out.println(off.isEqual(location,semesterCode));
                    return off.isEqual(location,semesterCode);})
                .findFirst();
    }
    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", catalogNumber='" + catalogNumber + '\'' +
                '}';
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public SimpleCourse toJson(){
        return new SimpleCourse(this);
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getNewWatcherId(){
        return watchers.size();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId == course.courseId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(courseId, catalogNumber);
    }
}
