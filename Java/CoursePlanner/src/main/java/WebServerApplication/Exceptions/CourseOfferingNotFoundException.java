package WebServerApplication.Exceptions;

public class CourseOfferingNotFoundException extends Exception {
    public CourseOfferingNotFoundException(final String message){


        super(message);
        //System.out.println("throwing Offering not found error");
    }
}
