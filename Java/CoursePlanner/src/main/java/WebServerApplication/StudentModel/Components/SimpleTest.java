package WebServerApplication.StudentModel.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleTest {
    int test;

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    @JsonCreator
    SimpleTest(@JsonProperty("test") final int test){this.test=test;}
}
