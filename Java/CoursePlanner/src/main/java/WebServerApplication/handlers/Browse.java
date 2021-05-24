package WebServerApplication.handlers;


import WebServerApplication.Exceptions.CourseNotFoundException;
import WebServerApplication.Exceptions.CourseOfferingNotFoundException;
import WebServerApplication.Exceptions.DepartmentNotFoundException;
import WebServerApplication.StudentModel.Components.Course;
import WebServerApplication.StudentModel.Components.Department;
import WebServerApplication.StudentModel.Components.Offering;
import WebServerApplication.StudentModel.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static WebServerApplication.Exceptions.Errors.ErrorCourseNotFound;
import static WebServerApplication.Exceptions.Errors.ErrorDepartmentNotFound;
import static WebServerApplication.Exceptions.Errors.throwcourseOffering;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Component
public class Browse {
    @Autowired
    Model m;

    public Mono<ServerResponse> getSections(ServerRequest request) {

        int departmentId = Integer.valueOf(request.pathVariable("deptId"));
        int courseId = Integer.valueOf(request.pathVariable("courseId"));
        int offeringId = Integer.valueOf(request.pathVariable("offeringId"));

        //Optional<Department> d = m.findDepartment(departmentId);
        return  Flux.fromIterable(m.getDepartments()).
                filter(depts -> depts.getDeptId() == departmentId).singleOrEmpty().
                switchIfEmpty(ErrorDepartmentNotFound).flatMapMany(dept -> Flux.fromIterable(dept.getAllCourses())).
                filter(course -> course.getCourseId() == courseId).singleOrEmpty().switchIfEmpty(ErrorCourseNotFound).
                flatMapMany(course -> Flux.fromIterable(course.getAllOfferings())).
                filter(offering -> offering.getCourseOfferingId() == offeringId).singleOrEmpty().
                switchIfEmpty(throwcourseOffering).flatMapMany(offering -> Flux.fromIterable(offering.getSections())).
                collectList().flatMap(s -> ServerResponse.ok().
                contentType(APPLICATION_JSON).syncBody(s)).
                onErrorResume(DepartmentNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e))).
                onErrorResume(CourseNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e))).
                onErrorResume(CourseOfferingNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e)));
    }

    public Mono<Department> processDepartment(int departmentId){
        return Flux.fromIterable(m.getDepartments()).
                filter(depts -> depts.getDeptId() == departmentId).
                singleOrEmpty().switchIfEmpty(ErrorDepartmentNotFound);
    }


    public Mono<ServerResponse> getCourseOfferings(ServerRequest request) {
        int departmentId = Integer.valueOf(request.pathVariable("deptId"));
        int courseId = Integer.valueOf(request.pathVariable("courseId"));

        return  processDepartment(departmentId).flatMapMany(dept -> Flux.fromIterable(dept.getAllCourses())).
                filter(course -> course.getCourseId() == courseId).singleOrEmpty().switchIfEmpty(ErrorCourseNotFound).
                flatMapMany(course -> Flux.fromIterable(course.getAllOfferings())).
                map(Offering::toJson).
                collectList().
                flatMap(s -> ServerResponse.ok().contentType(APPLICATION_JSON).syncBody(s)).
                onErrorResume(DepartmentNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e))).
                onErrorResume(CourseNotFoundException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e)));
    }

    public Mono<ServerResponse> getCourses(ServerRequest request) {

        class NotFoundBody{
            String field1 = "value1";
            String field2 = "value 2";
        }

        int departmentId = Integer.valueOf(request.pathVariable("deptId"));
        Function<ServerResponse.HeadersBuilder, ServerResponse.BodyBuilder> headerToBodyBuilder =
                headersBuilder -> (ServerResponse.BodyBuilder) (headersBuilder.header(
                        "Date:","Other Field"
                ));

        return processDepartment(departmentId).
                flatMapMany(dept -> Flux.fromIterable(dept.getAllCourses())).
                map(Course::toJson).
                collectList().flatMap(s -> ServerResponse.ok().contentType(APPLICATION_JSON).syncBody(s)).
                onErrorResume(e ->
                {
                    return headerToBodyBuilder.apply(ServerResponse.notFound()).
                            contentType(APPLICATION_JSON).body(Mono.just(new NotFoundBody()), NotFoundBody.class).single();
                });
    }

    public Mono<ServerResponse> getDepartments(ServerRequest request) {

        return  Flux.fromIterable(m.getDepartments()).
                map(Department::toJson).
                collectList().flatMap(s -> ServerResponse.ok().contentType(APPLICATION_JSON).syncBody(s));
    }






//
//    public Mono<ServerResponse> getDepartments(ServerRequest request){
//        System.out.println("Executing getDepartments!!!!!!! \n");
//
//        Flux<Department> departments = Flux.fromIterable(m.getAllDepartments());
//
//        return ServerResponse.ok().
//                contentType(MediaType.TEXT_PLAIN).body(departments,Department.class).
//                onErrorResume(Exception.class, e -> ServerResponse.notFound().build());
//    }

}
