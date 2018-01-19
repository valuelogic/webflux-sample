package pl.adomanski.webflux;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;

import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.all;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class SampleJava {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(WebFluxConfigurer.class);
        context.refresh();

        RouterFunction<ServerResponse> helloWorld = RouterFunctions.route(all(),
                request -> ok().body(Mono.just("Hello world"), String.class));

        RouterFunction<ServerResponse> beans = RouterFunctions.route(GET("/beans"),
                request -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(context.getBeanDefinitionNames()), String[].class));

        RouterFunction<ServerResponse> planets = RouterFunctions.route(GET("/planets"),
                request -> ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(Planets.INSTANCE.getSOLAR_SYSTEM()), new ParameterizedTypeReference<List<Planet>>() {}));

        RouterFunction<ServerResponse> problem = RouterFunctions.route(GET("/problem"),
                request -> {
                    ThrowableProblem tp = Problem.builder().withStatus(Status.INTERNAL_SERVER_ERROR).build();
                    return ServerResponse.status(HttpStatus.valueOf(tp.getStatus().getStatusCode()))
                            .contentType(new MediaType("application", "problem+json"))
                            .body(Mono.just(tp), ThrowableProblem.class);
                });

        HttpHandler handler = WebHttpHandlerBuilder
                .webHandler(RouterFunctions
                        .toWebHandler(beans.and(planets).and(problem).andOther(helloWorld),
                                context.getBean(MinimalHandlerStrategies.class)))
                .build();

        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
        HttpServer.create(8081).startAndAwait(adapter);
    }

}
