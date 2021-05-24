package WebServerApplication.Students;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.util.context.Context;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Queue;

public class SWatcher implements Subscriber<String> {

    Context context;
    int depId;
    int courseId;
    List<String> messages;

    SWatcher(int depId,int courseId,List<String> messages){
            this.depId=depId;
            this.courseId=courseId;
            this.messages=messages;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(1000000000);
        System.out.print("request 10");

    }

    @Override
    public void onNext(String s) {

    }

    @Override
    public void onError(Throwable t) {

    }


    public Context currentContext() {
        System.out.print("GETTING CURRENT CONTX\n");
        return context;
    }



    @Override
    public void onComplete() {

    }
}
