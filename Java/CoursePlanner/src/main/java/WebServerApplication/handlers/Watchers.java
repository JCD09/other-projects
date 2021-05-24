package WebServerApplication.handlers;


//TODO hopefully tomorrow;

import WebServerApplication.StudentModel.Components.Watcher;
import WebServerApplication.StudentModel.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class Watchers {

    @Autowired
    Model m;
    
}
