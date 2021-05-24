package WebServerApplication.handlers;

import WebServerApplication.Messages.AboutMessage;
import WebServerApplication.StudentModel.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class About {

    @Autowired
    Model m;

    public Mono<ServerResponse> aboutMessage(ServerRequest request){

        return Mono.just(new AboutMessage()).
                flatMap(message->ServerResponse.ok().contentType(APPLICATION_JSON).syncBody(message));
    }

    public Mono<ServerResponse> dumpModel(ServerRequest request){

        return Mono.just(new String("")).
                flatMap(message->ServerResponse.ok().contentType(APPLICATION_JSON).syncBody(message));
    }

}
