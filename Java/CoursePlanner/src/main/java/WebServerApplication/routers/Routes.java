package WebServerApplication.routers;

import WebServerApplication.Exceptions.CourseNotFoundException;
import WebServerApplication.Exceptions.DepartmentNotFoundException;
import WebServerApplication.Messages.WMessage;
import WebServerApplication.StudentModel.Components.Course;
import WebServerApplication.StudentModel.Components.Department;
import WebServerApplication.StudentModel.Components.Watcher;
import WebServerApplication.StudentModel.Model;
import WebServerApplication.handlers.About;
import WebServerApplication.handlers.Browse;
import WebServerApplication.handlers.Graph;
import WebServerApplication.handlers.PostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import org.springframework.http.HttpStatus;
import reactor.util.function.Tuples;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static WebServerApplication.Exceptions.Errors.ErrorCourseNotFound;
import static WebServerApplication.Exceptions.Errors.ErrorDepartmentNotFound;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class Routes {


    @Autowired Browse Browse;
    @Autowired Graph Graph;
    @Autowired PostRequest PostRequest;
    @Autowired About About;
    @Autowired Model m;

    private static final String API = "/api";
    private static final String DEPARTMENT = "/departments";
    private static final String COURSES = "/{deptId}/courses";
    private static final String OFFERINGS = "/{courseId}/offerings";
    private static final String OFFERING_ID = "/{offeringId}";

    Predicate<String> hasValue = Pattern.compile("[0-9]{1,10}").asPredicate();

    public Mono<Department> getDepartment(int departmentId){
        return Flux.fromIterable(m.getDepartments()).
            filter(depts -> depts.getDeptId() == departmentId).
            singleOrEmpty().switchIfEmpty(ErrorDepartmentNotFound);
    }

    public Mono<ServerResponse> addWatcher(ServerRequest request){
        WMessage w = request.bodyToMono(WMessage.class).block();

        int deptId = w.getDeptId();
        int courseId = w.getCourseId();

        Watcher ww = new Watcher();
        
        return
            getDepartment(deptId).  
                flatMapMany(dept -> Mono.just(Tuples.of(dept.toJson(),dept)).
                flatMapMany(tuple -> Flux.fromIterable(tuple.getT2().getAllCourses()).
                    filter(course -> course.getCourseId() == courseId).singleOrEmpty().
                    switchIfEmpty(ErrorCourseNotFound).
                    map(x -> Tuples.of(tuple.getT1(),x))))
                .map(x -> {ww.setId(x.getT2().getCourseId());ww.setDepartment(x.getT1()); return x.getT2();})
                .map(y -> {ww.setCourse(y.toJson()); return ww;})
                .collectList()
                .flatMap(s -> ServerResponse.ok()
                .contentType(APPLICATION_JSON).syncBody(s))
                .onErrorResume(
                    DepartmentNotFoundException.class, e -> 
                    Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e)))
                .onErrorResume(CourseNotFoundException.class, e -> 
                    Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e)));
        }

        @Bean
        public RouterFunction routerFunction() {

            return 
                nest(accept(APPLICATION_JSON),
                nest(path("/api"),
                    nest(path("/departments"),
                        nest(path("/{deptId}/courses"),
                            nest(path("/{courseId}/offerings"),
                                nest(path("/{offeringId}"),
                                    route(method(GET), Browse::getSections))
                                .andRoute(method(GET), Browse::getCourseOfferings))
                            .andRoute(method(GET), sr-> Browse.getCourses(sr)))
                        .andRoute(method(GET), Browse::getDepartments))
                        .andNest(path("/about"),
                            route(method(GET), About::aboutMessage))
                        .andNest(path("/watchers"),
                            route(method(POST),this::addWatcher))
                        .andNest(path("/stats/students-per-semester"),
                            nest(queryParam("deptId",hasValue),
                                route(method(GET), Graph::getGraphData)))
                        .andNest(path("/addoffering"),
                                route(method(POST), PostRequest::addOffering)))).
             and (resources("/**", new ClassPathResource("/static")));

        }

    class ResponseStatusExceptionWithExtraFields extends ResponseStatusException {
        String exceptionName;
        String path;
        ResponseStatusExceptionWithExtraFields(HttpStatus status,String reason, Throwable clause){
            super(status,reason, clause);
        }

        public void addName(String exceptionName,String path){
            this.exceptionName=exceptionName;
            this.path=path;
        }

    }
}


