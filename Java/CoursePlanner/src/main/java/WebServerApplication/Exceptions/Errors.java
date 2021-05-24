package WebServerApplication.Exceptions;

import WebServerApplication.StudentModel.Components.*;
import reactor.core.publisher.Mono;

public final class Errors {

    // TODO Rewrite tehse as functions that return Publushers with customized messages;
    //
    public static Mono<Department> ErrorDepartmentNotFound = Mono.error(new DepartmentNotFoundException("Department Not Found \n"));
    public static Mono<Course> ErrorCourseNotFound = Mono.error(new CourseNotFoundException("Course Not Found\n"));
    public static Mono<Offering> throwcourseOffering = Mono.error(new CourseOfferingNotFoundException("Offering Not Found\n"));
    public static Mono<JsonRecord> ErrorInvalidPostRecord = Mono.error(new InvalidPostRecordException("Error processing post request \n"));
    public static Mono<SimpleDepartment> departmentNotFoundError = Mono.error(new DepartmentNotFoundException("Department Not Found"));



}
