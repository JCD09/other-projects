package WebServerApplication.handlers;

import WebServerApplication.StudentModel.Components.JsonRecord;
import WebServerApplication.StudentModel.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static WebServerApplication.Exceptions.Errors.ErrorInvalidPostRecord;



@Component
public class PostRequest {

    @Autowired
    Model m;

    public Mono<ServerResponse> addOffering(ServerRequest request){
        System.out.print("we are here!!!!!!\n");

        // request.attributes().keySet().stream().forEach(System.out::println);
        request.bodyToMono(JsonRecord.class).switchIfEmpty(ErrorInvalidPostRecord).
                map(jsonRecord->jsonRecord.toRecord()).subscribe(record -> m.incorporateRecord(record));
        return ServerResponse.ok().build();
    }
}
