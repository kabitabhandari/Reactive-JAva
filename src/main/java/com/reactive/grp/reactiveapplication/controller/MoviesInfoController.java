package com.reactive.grp.reactiveapplication.controller;


import com.reactive.grp.reactiveapplication.domain.MovieInfo;
import com.reactive.grp.reactiveapplication.service.MoviesInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/v1")
@Slf4j
public class MoviesInfoController {

    private final MoviesInfoService moviesInfoService;


    public MoviesInfoController(MoviesInfoService moviesInfoService) {
        this.moviesInfoService = moviesInfoService;
    }

    @GetMapping("/movieinfos")
    public Flux<MovieInfo> getAllMovieInfos(@RequestParam(value = "year", required = false) Integer year) {

        log.info("year : {} ", year);
        if (year != null) {
            return moviesInfoService.getMovieInfoByYear(year).log();
        }
        return moviesInfoService.getAllMovieInfos();
    }

//    @GetMapping("/movieinfos/{id}")
//    public Mono<MovieInfo> getMovieInfoById(@PathVariable String id) {
//        return moviesInfoService.getMovieInfoById(id);
//    }


    @GetMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById_approach2(@PathVariable("id") String id) {

        // find returns Mono<T> so we can use map to set response status code. Also see line 65
        return moviesInfoService.getMovieInfoById(id)
                .map(m -> ResponseEntity.accepted().body(m))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build())) // fallback
                .log();
    }


    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    //if not mentioned @ResponseStatus, it gives 200ok status code for get,post,delete etc
    public Mono<MovieInfo> addMovieInfo(@RequestBody MovieInfo movieInfo) {
        return moviesInfoService.addMovieInfo(movieInfo);
    }

    @DeleteMapping("/movieinfos/{id}")
    public Mono<ResponseEntity> deleteMovieInfoById(@PathVariable String id) {


        //Note: delete returns Mono<void> so dont use map to set response status code. Instead use then(mono.just). Also see line 47
        //return moviesInfoService.deleteMovieInfoById(id).map(m -> ResponseEntity.noContent().build());  // deletes but status is still 200 ok

        return moviesInfoService.deleteMovieInfoById(id).then(Mono.just(ResponseEntity.noContent().build()));  //works
    }

}
