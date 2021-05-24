package WebServerApplication.StudentModel;

import WebServerApplication.Exceptions.CourseNotFoundException;
import WebServerApplication.Exceptions.DepartmentNotFoundException;
import WebServerApplication.StudentModel.Components.*;
import WebServerApplication.StudentModel.Components.Record;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class Model {

    private Model m;
    private FileReader fileReader;

    private List<Department> departments;
    private List<Record> listOfRecords;

    // Name is the keyName of the query paramters
    // Predicate is equals;
    // Code taken from:
    // https://www.javatips.net/api/spring-framework-master/spring-webflux/src/main/java/org/
    // springframework/web/reactive/function/server/RequestPredicates.java
    //
    // lambdas with return statement;

    public static RequestPredicate queryParam(String name, Predicate<String> predicate) {
        return request -> {
            Optional<String> s = request.queryParam(name);
            return s.filter(predicate).isPresent();
        };
    }

    public static void main(String[] args){
        Model m=new Model();
        int id = 0;

//        Function<List<Section>,Section> flattenSections =
//                sections->sections.stream().
//                        dropWhile(section->section.equals("LEC")).
//                        reduce(new Section(),Section::combine);
//
//        Function<Tuple2<Offering,Section>,Integer> keyMapper = tuple -> tuple.getT1().getSemesterCode();
//        Function<Tuple2<Offering,Section>,Integer> valueMapper = tuple -> tuple.getT2().getEnrollmentTotal();
//        class GraphMessage{
//              int semesterCode;

//            @Override
//            public String toString() {
//                return "GraphMessage{" +
//                        "semesterCode=" + semesterCode +
//                        ", totalCoursesTaken=" + totalCoursesTaken +
//                        '}';
//            }
//
//            int totalCoursesTaken;
//
//              GraphMessage(int semesterCode, int totalCoursesTaken){
//                  this.semesterCode=semesterCode;
//                  this.totalCoursesTaken=totalCoursesTaken;
//            }
//
//            GraphMessage update(int numberOfStudents){
//                  totalCoursesTaken=totalCoursesTaken+numberOfStudents;
//                  return this;
//            }
//        }

//        Function <Tuple2<Integer,Integer>,GraphMessage> toGM = tuple-> new GraphMessage(tuple.getT1(),tuple.getT2());
//        BiFunction<GraphMessage,Integer,GraphMessage> enrollmentAggregator =
//                (gtotal,newgtotal)-> gtotal.update(newgtotal);


//        Flux.fromIterable(m.getDepartments()).filter(dep->dep.getDeptId()==id).singleOrEmpty().
//                switchIfEmpty(Mono.error(new DepartmentNotFoundException("department with id not found"))).
//                flatMapMany(dept->Flux.fromIterable(dept.getAllCourses())).
//                switchIfEmpty(Mono.error(new CourseNotFoundException("courses are not present"))).
//                flatMapIterable(course->course.getAllOfferings()).
//                map(offering -> Tuples.of(offering,flattenSections.apply(offering.getSections()))).
//                groupBy(offering->keyMapper.apply(offering),offering->valueMapper.apply(offering)).
//                flatMap(a-> a.reduce(new GraphMessage(a.key(),0),enrollmentAggregator)).
//
//                subscribe(input-> System.out.print(input+"\n"));

    }

    public Model(){
        System.out.print("MODEL CREATED!!!!!\n");
        this.fileReader = new FileReader();
        this.departments = new ArrayList<>();
        fileReader.records.subscribe(rec -> incorporateRecord(rec));
    }

    Set<Integer> i = new HashSet<>();
    public void incorporateRecord(Record record){
        int index = departments.size();
        Department department = findDepartment(record).orElseGet(
                ()->{Department newDepartment = new Department(index,record);
                    departments.add(newDepartment);
                    return newDepartment;}
        );
        department.addCourse(record);
    }


    public Optional<Department> findDepartment(int departmentID){
        return departments.
                stream().
                filter(d->d.getDeptId()==departmentID).
                findFirst();
    }

    public List<Department> getDepartments(){return this.departments;}


//    public static void main(String[] args) {
//        //System.out.println(m);
//
//        m.departments.forEach(department -> {
//
//            ObjectMapper mapper = new ObjectMapper();
//            try{
//
//                String jsonInString = mapper.writerWithDefaultPrettyPrinter().
//                        writeValueAsString(department.getAllCourses().get(0).getAllOfferings());
//                System.out.println(jsonInString);
//            }
//            catch (JsonProcessingException e){}
//        });
//
//    }

    private Optional<Department> findDepartment(Record record){
        return departments.
                stream().
                filter(dep ->dep.getName().equals(record.subjectName)).
                findFirst(); };
}
