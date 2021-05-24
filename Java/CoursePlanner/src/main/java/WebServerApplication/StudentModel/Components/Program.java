package WebServerApplication.StudentModel.Components;

import java.util.Optional;

public class Program {
    public static void main(String[] args) {

        // Create an empty Optional.
        Optional<Integer> option = Optional.of(null);
        // Use orElse to get value or the argument if no value is present.
        int result = option.orElse(100);
        System.out.println(result);
    }
}