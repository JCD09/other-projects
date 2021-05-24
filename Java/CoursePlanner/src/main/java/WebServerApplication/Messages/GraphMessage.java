package WebServerApplication.Messages;

import java.util.Comparator;

public class GraphMessage implements Comparable<GraphMessage> {


    int semesterCode;


    public int compareTo(GraphMessage gm){
        if(this.semesterCode<gm.getSemesterCode())return -1;
        else if(this.semesterCode==gm.getSemesterCode()) return 0;
        else{ return 1;}
    }

    @Override
    public String toString() {
        return "GraphMessage{" +
                "semesterCode=" + semesterCode +
                ", totalCoursesTaken=" + totalCoursesTaken +
                '}';
    }

    int totalCoursesTaken;

    public GraphMessage(int semesterCode, int totalCoursesTaken){
        this.semesterCode=semesterCode;
        this.totalCoursesTaken=totalCoursesTaken;
    }

    public GraphMessage update(int numberOfStudents){
        totalCoursesTaken=totalCoursesTaken+numberOfStudents;
        return this;
    }

    public int getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(int semesterCode) {
        this.semesterCode = semesterCode;
    }

    public int getTotalCoursesTaken() {
        return totalCoursesTaken;
    }

    public void setTotalCoursesTaken(int totalCoursesTaken) {
        this.totalCoursesTaken = totalCoursesTaken;
    }
}