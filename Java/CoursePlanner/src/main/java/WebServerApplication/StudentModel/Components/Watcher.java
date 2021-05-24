package WebServerApplication.StudentModel.Components;

import WebServerApplication.StudentModel.Components.SimpleCourse;
import WebServerApplication.StudentModel.Components.SimpleDepartment;
import org.apache.poi.ss.formula.functions.T;
import reactor.core.Disposable;
import reactor.core.publisher.FluxSink;
import reactor.util.context.Context;

import java.util.List;
import java.util.Queue;
import java.util.function.LongConsumer;
import WebServerApplication.StudentModel.Components.SimpleCourse;
import WebServerApplication.StudentModel.Components.SimpleDepartment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class Watcher {
    int id;
    SimpleDepartment department;
    SimpleCourse course;

    @Override
    public String toString() {
        return "WatcherRequest{" +
                "id=" + id +
                ", department=" + department +
                ", course=" + course +
                ", events=" + events +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SimpleDepartment getDepartment() {
        return department;
    }

    public void setDepartment(SimpleDepartment department) {
        this.department = department;
    }

    public SimpleCourse getCourse() {
        return course;
    }

    public void setCourse(SimpleCourse course) {
        this.course = course;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public void addEvent(String message){
        events.add(message);
    }

    List<String> events;

    @JsonCreator
    public Watcher(
            @JsonProperty("id") int id,
            @JsonProperty("department") SimpleDepartment department,
            @JsonProperty("course")     SimpleCourse course,
            @JsonProperty("list")       List<String> events){
        this.id = id;
        this.department = department;
        this.course = course;
        this.events = events;


    }

    public Watcher(){

    }
}
