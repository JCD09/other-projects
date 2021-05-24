package WebServerApplication.StudentModel.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleDepartment {
        int deptId;
        String name;

    @Override
    public String toString() {
        return "SimpleDepartment{" +
                "deptId=" + deptId +
                ", name='" + name + '\'' +
                '}';
    }

    @JsonCreator
    public SimpleDepartment(
            @JsonProperty("D") Department d) {

        this.deptId = d.getDeptId();
        this.name = d.getName();
        }


    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
