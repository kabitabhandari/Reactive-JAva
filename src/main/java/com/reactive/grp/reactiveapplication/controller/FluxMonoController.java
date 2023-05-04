package com.reactive.grp.reactiveapplication.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxMonoController {


    @GetMapping("/flux")
    public Flux<Integer> flux() {
        return Flux.just(11, 21, 31)
                .log();
    }

    @GetMapping("/mono")
    public Mono<String> helloWorldMono() {
        return Mono.just("hello-world-beginners");
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> stream() {
        return Flux.interval(Duration.ofSeconds(2))
                .log();
    }
}