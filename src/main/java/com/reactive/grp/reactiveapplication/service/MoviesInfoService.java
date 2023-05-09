package com.reactive.grp.reactiveapplication.service;


import com.reactive.grp.reactiveapplication.domain.MovieInfo;
import com.reactive.grp.reactiveapplication.repository.MovieInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MoviesInfoService {


    private MovieInfoRepository movieInfoRepository;

    public MoviesInfoService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Flux<MovieInfo> getAllMovieInfos(){

        return  movieInfoRepository.findAll();
    }

    public Flux<MovieInfo> getMovieInfoByYear(Integer year){

        return movieInfoRepository.findByYear(year);
    }

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        log.info("addMovieInfo : {} " , movieInfo );
        Mono<MovieInfo> saved =  movieInfoRepository.save(movieInfo)
                .log();
        return saved;
    }

    public Mono<MovieInfo> getMovieInfoById(String id) {
        return movieInfoRepository.findById(id);
    }



    public Mono<Void> deleteMovieInfoById(String id) {
        Mono<Void> result = movieInfoRepository.deleteById(id);
        return result;



    }
}
