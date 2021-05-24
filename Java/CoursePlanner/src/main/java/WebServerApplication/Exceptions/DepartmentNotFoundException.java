package WebServerApplication.Exceptions;

public class DepartmentNotFoundException extends Exception {
    public DepartmentNotFoundException(final String message){

        super(message);
        //System.out.println("throwing Department not found error");
    }
}
