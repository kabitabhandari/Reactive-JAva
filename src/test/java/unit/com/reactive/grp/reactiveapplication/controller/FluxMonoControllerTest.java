package com.reactive.grp.reactiveapplication.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@WebFluxTest(controllers = FluxMonoController.class)
class FluxMonoControllerTest {
    //We would want to do @autowire WebTestClient in test class here, because we are intentionally injecting
    // it to this test class so that it can send request to the application (to various endpoints).
    // Note: @mock WebTestClient  annotation will not work here. It will give us NullPointerException.
    // Because WebTestClient is a Sprint Component and it gets initialized when the application context is loading.
    // But if we don't use autowire annotation to it then it's bean/object  wont be created making it point to null.


    //Mocking is the act of removing external dependencies from a unit test in order to create
    // a controlled environment around it. Typically, we mock all other classes that interact with
    // the class that we want to test.But WebTestClient is not interacting in our controller class, we are
    // injecting it here (in this class) using annotation @Autowired so that we could call the controller endpoints.
    @Autowired
    WebTestClient webTestClient;

    private static final String GET_FLUX_ENDPOINT = "/flux";
    private static final String GET_MONO_ENDPOINT = "/mono";


    @Test
    void flux() {

        webTestClient
                .get()
                .uri(GET_FLUX_ENDPOINT)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(3);

    }


    @Test
    void flux_approach2() {

        var flux = webTestClient
                .get()
                .uri(GET_FLUX_ENDPOINT)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(flux)
                .expectNext(8, 9, 4)
                .expectComplete();

    }



    @Test
    void flux_approach3() {
        webTestClient
                .get()
                .uri(GET_FLUX_ENDPOINT)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .consumeWith(entityExchangeResult -> {
                    var responseBody = entityExchangeResult.getResponseBody();
                    assert (responseBody != null ? responseBody.size() : 0) ==3;
                });

    }

    @Test
    void mono() {

        webTestClient
                .get()
                .uri(GET_MONO_ENDPOINT)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(String.class)
                .hasSize(1);
    }


    @Test
    void mono_approach2() {

        var mono = webTestClient
                .get()
                .uri(GET_MONO_ENDPOINT)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier.create(mono)
                .expectNext("yummy food!")
                .expectComplete();

    }



    @Test
    void mono_approach3() {
        webTestClient
                .get()
                .uri(GET_MONO_ENDPOINT)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(String.class)
                .consumeWith(c -> {
                    var responseBody = c.getResponseBody();
                    assert (responseBody != null ? responseBody.size() : 0) ==1;
                });

    }
}