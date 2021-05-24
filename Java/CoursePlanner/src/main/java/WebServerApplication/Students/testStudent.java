package WebServerApplication.Students;

import WebServerApplication.StudentModel.Components.Course;
import WebServerApplication.StudentModel.Components.Offering;
import WebServerApplication.StudentModel.Components.Record;
import WebServerApplication.StudentModel.Components.SimpleCourse;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.ws.rs.container.Suspended;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class testStudent {

    @Test
    public void testOfferingSubscription() {
        String[] s = {"Instructors","Instructors2"};


        Record record = new Record.NestedRecordBuilder()
                .addSemesterCode(1234).
                addSubject("CMPT").
                addCatalogueNumber("123")
                .addLocation("BURNABY").
                addEnrollment(90,90).
                addComponentCode("TUT").
                addInstructors(new ArrayList<String>(Arrays.asList(s))).createNewRecord();
        Course course = new Course(90,record);
    }

}
