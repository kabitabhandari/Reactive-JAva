package com.reactive.grp.reactiveapplication.controller;

import com.reactive.grp.reactiveapplication.domain.MovieInfo;
import com.reactive.grp.reactiveapplication.repository.MovieInfoRepository;
import com.reactive.grp.reactiveapplication.service.MoviesInfoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureWebTestClient
class MoviesInfoControllerIntgTest {
    @Autowired
    MovieInfoRepository movieInfoRepository;
    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        movieInfoRepository.saveAll(mockedMovieInfo()).blockLast();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addMovieInfo() {
        MovieInfo postOneMovieAsMock = new MovieInfo("abc", "Dark Knight Rises",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        webTestClient.post()
                .uri("/v1/movieinfos")
                .bodyValue(postOneMovieAsMock) // body of Post call (json object)
                .exchange() // makes call to endpoint
                .expectStatus()
                .isCreated()
                .expectBodyList(MovieInfo.class) // return type of endpoint
                .consumeWith(new Consumer<EntityExchangeResult<List<MovieInfo>>>() { // assert response values
                    @Override
                    public void accept(EntityExchangeResult<List<MovieInfo>> listEntityExchangeResult) {
                        Assertions.assertEquals("Dark Knight Rises", listEntityExchangeResult.getResponseBody().get(0).getName());
                        assert listEntityExchangeResult.getResponseBody() != null;
                        assert listEntityExchangeResult.getResponseBody().get(0).getMovieInfoId()!= null;
                    }
                });
    }

    @Test
    void getAllMovieInfo() {

        WebTestClient.ListBodySpec<MovieInfo> movieInfoListBodySpec = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/movieinfos").build())
                .exchange() // makes call to endpoint
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class) // return type of endpoint
                .consumeWith(new Consumer<EntityExchangeResult<List<MovieInfo>>>() { // assert response values
                    @Override
                    public void accept(EntityExchangeResult<List<MovieInfo>> listEntityExchangeResult) {
                        assert listEntityExchangeResult.getResponseBody() != null;
                        assert listEntityExchangeResult.getResponseBody().get(0).getMovieInfoId()!= null;
                    }
                });
    }
    @Test
    void getAllMovieInfoWithQueryParamYear() {

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/movieinfos")
                        .queryParam("year", "2023")
                        .build())
                .exchange() // makes call to endpoint
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class) // return type of endpoint
                .consumeWith(new Consumer<EntityExchangeResult<List<MovieInfo>>>() { // assert response values
                    @Override
                    public void accept(EntityExchangeResult<List<MovieInfo>> listEntityExchangeResult) {
                        assert listEntityExchangeResult.getResponseBody() != null;
                        assert listEntityExchangeResult.getResponseBody().get(0).getMovieInfoId()!= null;
                        assert listEntityExchangeResult.getResponseBody().get(0).getYear() != null;

                    }
                });





    }


    private List<MovieInfo> mockedMovieInfo() {
        List<MovieInfo> hugeList = new ArrayList<>();

        MovieInfo movieInfo1 = new MovieInfo();
        movieInfo1.setName("Chaos Walking");
        movieInfo1.setYear(2021);
        movieInfo1.setRelease_date(LocalDate.parse("2021-06-15"));
        movieInfo1.setCast(List.of("Daisy Ridley", "Nick Jonas", "Tom Holland"));
        movieInfo1.setMovieInfoId(null);

        hugeList.add(movieInfo1);

        MovieInfo movieInfo2 = new MovieInfo();
        movieInfo2.setName("Jumanji");
        movieInfo2.setYear(2019);
        movieInfo2.setRelease_date(LocalDate.parse("2019-07-08"));
        movieInfo2.setCast(List.of("Dwayne Johnson", "Nick Jonas", "Kevin Hart"));
        movieInfo2.setMovieInfoId("abc");

        hugeList.add(movieInfo2);

        MovieInfo movieInfo3 = new MovieInfo();
        movieInfo3.setName("Goat");
        movieInfo3.setYear(2016);
        movieInfo3.setRelease_date(LocalDate.parse("2016-02-25"));
        movieInfo3.setCast(List.of("James Franco", "Nick Jonas", "Gus Halper"));
        movieInfo3.setMovieInfoId("tty678");

        hugeList.add(movieInfo3);

        MovieInfo movieInfo4 = new MovieInfo();
        movieInfo3.setName("Laptop Destroy");
        movieInfo3.setYear(2023);
        movieInfo3.setRelease_date(LocalDate.parse("2023-02-25"));
        movieInfo3.setCast(List.of("Angelina", "Miley", "Adele"));
        movieInfo3.setMovieInfoId("hhg678");

        hugeList.add(movieInfo4);

        return hugeList;
    }
}