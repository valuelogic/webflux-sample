package pl.adomanski.webflux

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.all
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import reactor.ipc.netty.http.server.HttpServer

fun main(args: Array<String>) {

    val context = AnnotationConfigApplicationContext()
    context.scan("pl.adomanski")

    val beansRoute = route(
            RequestPredicates.GET("/beans"),
            HandlerFunction<ServerResponse> { _: ServerRequest ->
                ok().contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just<Array<String>>(context.beanDefinitionNames))
            })

    val helloWorld = route(all(),
            HandlerFunction<ServerResponse> { _: ServerRequest ->
                ok().body(Mono.just("Hello World"))
            })

    val handler = toHttpHandler(beansRoute.andOther(helloWorld))

    val adapter = ReactorHttpHandlerAdapter(handler)
    HttpServer.create(8080).startAndAwait(adapter)
}

