package com.reactive.grp.reactiveapplication.repository;

import com.reactive.grp.reactiveapplication.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles("test")
public class MoviesInfoRepositoryIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        movieInfoRepository.saveAll(mockedMovieInfo()).blockLast();
    }


    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAll() {

        var moviesFlux = movieInfoRepository.findAll().log();

        StepVerifier.create(moviesFlux)
                .expectNextCount(3)
                .verifyComplete();

    }

    @Test
    void findById() {

        var movieInfoMono = movieInfoRepository.findById("abc");

        StepVerifier.create(movieInfoMono)
                .assertNext(m -> {
                    assertEquals("Jumanji", m.getName());
                }).verifyComplete();
    }

    @Test
    void saveMovieInfo() {

        var newMovieToBeAdded = new MovieInfo(null, "Jumanji RETURNS", 2022, List.of("Nick", "Kevin"), LocalDate.parse("2022-06-15"));

        var savedMovieInfo = movieInfoRepository.save(newMovieToBeAdded).log();
        // note when saved it will give id on its own as it is a Primary key in our embedded mongo db.

        StepVerifier.create(savedMovieInfo)
                .assertNext(m -> {
                    assertNotNull(m.getMovieInfoId());
                    assertNotNull(m.getMovieInfoId());
                })
                .verifyComplete();

    }

    @Test
    void updateMovieInfo() {

        var movieInfo = movieInfoRepository.findById("abc").block();
        movieInfo.setYear(2097);

        var savedMovieInfo = movieInfoRepository.save(movieInfo);

        StepVerifier.create(savedMovieInfo)
                .assertNext(m -> {
                    assertNotNull(m.getMovieInfoId());
                    assertEquals(Integer.valueOf(2097), m.getYear());
                })
                .verifyComplete();

    }

    @Test
    void deleteMovieInfo() {

        movieInfoRepository.deleteById("abc").block();

        var movieInfos = movieInfoRepository.findAll();

        StepVerifier.create(movieInfos)
                .expectNextCount(2)
                .verifyComplete();

    }

    @Test
    void findMovieInfoByYear() {

        var movieInfosFlux = movieInfoRepository.findByYear(2021).log();

        StepVerifier.create(movieInfosFlux)
                .expectNextCount(1)
                .verifyComplete();



    }

    @Test
    void findByName() {

        var movieInfosMono = movieInfoRepository.findByName("Chaos Walking").log();

        StepVerifier.create(movieInfosMono)
                .expectNextCount(1)
                .verifyComplete();


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

        return hugeList;
    }

}
