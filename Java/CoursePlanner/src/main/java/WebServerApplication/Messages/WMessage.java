package WebServerApplication.Messages;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WMessage {
    int deptId;
    int courseId;

    @JsonCreator
    public WMessage(@JsonProperty ("deptId") int deptId,
                    @JsonProperty ("courseId") int courseId) {
        this.deptId = deptId;
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "WMessage{" +
                "deptId=" + deptId +
                ", courseId=" + courseId +
                '}';
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
