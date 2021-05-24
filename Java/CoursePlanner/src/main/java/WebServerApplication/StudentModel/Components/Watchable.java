package WebServerApplication.StudentModel.Components;

public interface Watchable<T,P>{

    void notifyWatchers(P message);

}
