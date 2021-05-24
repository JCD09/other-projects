package WebServerApplication.StudentModel.Components;


import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.LinkedList;
import java.util.Queue;

public class Section {



    String type;
    int enrollmentCap;
    int enrollmentTotoal;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotoal;
    }

    public void setEnrollmenttotoal(int enrollmenttotoal) {
        this.enrollmentTotoal = enrollmenttotoal;
    }

    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    public void setEnrollmentCap(int enrollmentCap) {
        this.enrollmentCap = enrollmentCap;
    }

    @Override
    public String toString() {
        return "Section{" +
                "type='" + type + '\'' +
                ", enrollmentCap=" + enrollmentCap +
                ", enrollmentTotal=" + enrollmentTotoal +
                '}';
    }

    public Section(Record record) {
        this.type = record.component;
        this.enrollmentTotoal = record.enrollmentTotal;
        this.enrollmentCap = record.enrollmentCap;
    }

    public Section(){
        this.type = "LEC";
        this.enrollmentCap=0;
        this.enrollmentTotoal=0;
    }

    public Section combine(Section otherSection){
        assert this.type.equals(otherSection.type);
        this.enrollmentTotoal += otherSection.enrollmentTotoal;
        return this;
    }

}
