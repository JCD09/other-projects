package WebServerApplication.StudentModel.Components;

import WebServerApplication.StudentModel.Data.Validator;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class JsonRecord {


    public int semester;
    public String subjectName;
    public String catalogNumber;
    public String location;
    public int enrollmentCap;
    public int enrollmentTotal;
    public String instructor;
    public String component;

    @JsonCreator
    public JsonRecord(
            @JsonProperty("semester") int semester,
            @JsonProperty("subjectName") String subjectName,
            @JsonProperty("catalogNumber")String catalogNumber,
            @JsonProperty("location") String location,
            @JsonProperty("enrollmentCap")int enrollmentCap,
            @JsonProperty("enrollmentTotal")int enrollmentTotal,
            @JsonProperty("instructor") String instructors,
            @JsonProperty("component") String component) {
        this.semester = semester;
        this.subjectName = subjectName;
        this.catalogNumber = catalogNumber;
        this.location = location;
        this.enrollmentCap = enrollmentCap;
        this.enrollmentTotal = enrollmentTotal;
        this.instructor = instructors;
        this.component = component;
    }

    @Override
    public String toString() {
        return "JsonRecord{" +
                "semester=" + semester +
                ", subjectName='" + subjectName + '\'' +
                ", catalogNumber='" + catalogNumber + '\'' +
                ", location='" + location + '\'' +
                ", enrollmentCap=" + enrollmentCap +
                ", enrollmentTotal=" + enrollmentTotal +
                ", instructor='" + instructor + '\'' +
                ", component='" + component + '\'' +
                '}';
    }

    public int getSemester() {
        return semester;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public String getLocation() {
        return location;
    }

    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getComponent() {
        return component;
    }

    public Record toRecord(){
        List<String> instructors = Validator.addInstructors(Validator.removeQuotationMarks(instructor));
        return new Record.NestedRecordBuilder().
        addSemesterCode(semester).addSubject(subjectName).addCatalogueNumber(catalogNumber).addLocation(location).
                addEnrollment(enrollmentCap,enrollmentTotal).
                addInstructors(instructors).
                addComponentCode(component).
                createNewRecord();
    }


}
