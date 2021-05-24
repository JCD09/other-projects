package WebServerApplication.handlers;

import WebServerApplication.Exceptions.CourseNotFoundException;
import WebServerApplication.Exceptions.DepartmentNotFoundException;
import WebServerApplication.Messages.GraphMessage;
import WebServerApplication.StudentModel.Components.Offering;
import WebServerApplication.StudentModel.Components.Section;
import WebServerApplication.StudentModel.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static WebServerApplication.Exceptions.Errors.ErrorDepartmentNotFound;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class Graph {


    @Autowired
    Model m;

    Function<List<Section>,Section> flattenSections = sections->sections.stream().
            dropWhile(section->section.equals("LEC")).
            reduce(new Section(),Section::combine);

    Function<Tuple2<Integer,Integer>,GraphMessage> toGM = tuple-> new GraphMessage(tuple.getT1(),tuple.getT2());

    BiFunction<GraphMessage,Integer,GraphMessage> enrollmentAggregator = (gtotal,newgtotal)-> gtotal.update(newgtotal);

    Function<Tuple2<Offering,Section>,Integer> keyMapper = tuple -> tuple.getT1().getSemesterCode();

    Function<Tuple2<Offering,Section>,Integer> valueMapper = tuple -> tuple.getT2().getEnrollmentTotal();

    Comparator<GraphMessage> gm_comparator = Comparator.comparingInt(GraphMessage::getSemesterCode);


    // TODO: this can be done waay simpler; I do not know how to get query params to work correctly
    // function to work. so I set aggregation to all departments;




    public Mono<ServerResponse> getGraphData(ServerRequest request){
        System.out.print("Printing Query Paramters");
        int departmentId = Integer.valueOf(request.queryParam("deptId").get());

        return
                Flux.fromIterable(m.getDepartments()).filter(depts -> depts.getDeptId() == departmentId).singleOrEmpty().
                        switchIfEmpty(ErrorDepartmentNotFound).flatMapMany(dept->Flux.fromIterable(dept.getAllCourses())).
                        switchIfEmpty(Mono.error(new CourseNotFoundException("courses are not present"))).
                        flatMapIterable(course->course.getAllOfferings()).

                        map(offering -> Tuples.of(offering,flattenSections.apply(offering.getSections()))).

                        groupBy(offering->keyMapper.apply(offering),offering->valueMapper.apply(offering)).

                        flatMap(a-> a.reduce(new GraphMessage(a.key(),0),enrollmentAggregator)).

                        collectSortedList(gm_comparator)

                        .flatMap(s->ServerResponse.ok().contentType(APPLICATION_JSON).syncBody(s)).
                        onErrorResume(DepartmentNotFoundException.class, e ->
                                Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e)));


    }
}
