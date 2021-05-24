package WebServerApplication.Exceptions;

import WebServerApplication.StudentModel.Components.Course;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class CourseNotFoundException extends Exception {

    public static Function<String,Mono<Course>> courseNotFoundError =
            string -> Mono.error(new CourseNotFoundException(string));

    public CourseNotFoundException(final String message){
        super(message);
        //System.out.println("throwing Course not found error");

    }
}
