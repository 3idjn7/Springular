package com.example.crudspringular.service;

import com.example.crudspringular.entity.Movie;
import com.example.crudspringular.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Movie saveMovie(Movie movie) {
        log.info("Attempting to save movie: {}", movie.getTitle());
        Movie savedMovie = movieRepository.save(movie);
        log.info("Saved movie: {}", savedMovie.getTitle());
        return movieRepository.save(movie);
    }

    public List<Movie> findAllMovies() {
        log.info("Attempting to fetch all movies");
        List<Movie> movies = movieRepository.findAll();
        log.info("Fetched {} movies", movies.size());
        return movies;
    }

    public Optional<Movie> findMovieById(Long id) {
        log.info("Attempting to find movie with ID: {}", id);
        Optional<Movie> movie = movieRepository.findById(id);
        log.info("Found movie with ID {}: {}", id, movie.isPresent() ? "present" : "not present");
        return movie;
    }

    @Transactional
    public void deleteMovie(Long id) {
        log.info("Attempting to delete movie with ID: {}", id);
        movieRepository.deleteById(id);
        log.info("Deleted movie with ID: {}", id);
    }
}
