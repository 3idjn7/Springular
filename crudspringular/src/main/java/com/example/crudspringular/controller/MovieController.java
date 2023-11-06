package com.example.crudspringular.controller;

import com.example.crudspringular.entity.Movie;
import com.example.crudspringular.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        log.info("Received request to create movie: {}", movie.getTitle());
        Movie savedMovie = movieService.saveMovie(movie);
        log.info("Created movie with ID: {}", savedMovie.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        log.info("Received request to list all movies");
        List<Movie> movies = movieService.findAllMovies();
        log.info("Returning {} movies", movies.size());
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        log.info("Received request to get movie with ID: {}", id);
        Optional<Movie> movie = movieService.findMovieById(id);
        if (movie.isPresent()) {
            log.info("Returning movie with ID: {}", id);
            return ResponseEntity.ok(movie.get());
        } else {
            log.info("Movie with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.info("Received request to delete movie with ID: {}", id);
        movieService.deleteMovie(id);
        log.info("Deleted movie with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
