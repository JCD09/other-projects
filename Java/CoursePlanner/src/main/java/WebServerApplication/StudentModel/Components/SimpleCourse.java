package WebServerApplication.StudentModel.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class SimpleCourse {

    private int courseId; // this is just a number
    private String catalogNumber; // this is string corresponding to course IE 156;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleCourse)) return false;
        SimpleCourse that = (SimpleCourse) o;
        return courseId == that.courseId &&
                Objects.equals(catalogNumber, that.catalogNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(courseId, catalogNumber);
    }

    public int getCourseId() {

        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    @JsonCreator
    public SimpleCourse(@JsonProperty("courseId") int courseId,
    @JsonProperty("catalogNumber") String catalogNumber){
        this.courseId=courseId;
        this.catalogNumber=catalogNumber;
    }

    public SimpleCourse(Course record) {
        this.courseId = record.getCourseId();
        this.catalogNumber = record.getCatalogNumber();
    }
}
