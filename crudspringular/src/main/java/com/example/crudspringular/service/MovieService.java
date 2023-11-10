package com.example.crudspringular.service;

import com.example.crudspringular.dto.ActorDTO;
import com.example.crudspringular.dto.GenreDTO;
import com.example.crudspringular.dto.MovieDTO;
import com.example.crudspringular.entity.Actor;
import com.example.crudspringular.entity.Genre;
import com.example.crudspringular.entity.Movie;
import com.example.crudspringular.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreService genreService;
    private final ActorService actorService;

    @Autowired
    public MovieService(MovieRepository movieRepository, GenreService genreService, ActorService actorService) {
        this.movieRepository = movieRepository;
        this.genreService = genreService;
        this.actorService = actorService;
    }

    public MovieDTO saveMovie(MovieDTO movieDTO) {
        log.info("Attempting to save movie: {}", movieDTO.getTitle());
        Movie movie = convertToEntity(movieDTO);
        Movie savedMovie = movieRepository.save(movie);
        MovieDTO savedMovieDTO = convertToDto(savedMovie);
        log.info("Saved movie: {}", savedMovieDTO.getTitle());
        return savedMovieDTO;
    }

    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        log.info("Attempting to update movie with ID: {}", id);
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id " + id));

        // Fetch genres by their IDs in a single query
        List<Genre> genres = movieDTO.getGenres().stream()
                .map(genreDTO -> genreService.findGenreById(genreDTO.getId()))
                .collect(Collectors.toList());

        // Similarly, fetch actors by their IDs in a single query
        List<Actor> actors = movieDTO.getActors().stream()
                .map(actorDTO -> actorService.findActorById(actorDTO.getId()))
                .collect(Collectors.toList());

        // Update the movie entity
        movie.setTitle(movieDTO.getTitle());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setGenres(genres);
        movie.setActors(actors);

        Movie updatedMovie = movieRepository.save(movie);
        return convertToDto(updatedMovie);
    }

    public Page<MovieDTO> searchMovies(String searchTerm, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return movieRepository.findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(searchTerm, pageRequest)
                .map(this::convertToDto);
    }


    public Page<MovieDTO> findAllMovies(Pageable pageable) {
        log.info("Attempting to fetch all movies");
        return movieRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    public Optional<MovieDTO> findMovieById(Long id) {
        log.info("Attempting to find movie with ID: {}", id);
        Optional<MovieDTO> movieDTO = movieRepository.findById(id)
                .map(this::convertToDto);
        log.info("Found movie with ID {}: {}", id, movieDTO.isPresent() ? "present" : "not present");
        return movieDTO;
    }

    public void deleteMovie(Long id) {
        log.info("Attempting to delete movie with ID: {}", id);
        movieRepository.deleteById(id);
        log.info("Deleted movie with ID: {}", id);
    }

    private MovieDTO convertToDto(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setReleaseYear(movie.getReleaseYear());

        List<GenreDTO> genreDTOs = movie.getGenres().stream()
                .map(genre -> {
                    GenreDTO genreDTO = new GenreDTO();
                    genreDTO.setId(genre.getId());
                    genreDTO.setName(genre.getName());
                    return genreDTO;
                })
                .collect(Collectors.toList());
        movieDTO.setGenres(genreDTOs);

        List<ActorDTO> actorDTOs = movie.getActors().stream()
                .map(actor -> {
                    ActorDTO actorDTO = new ActorDTO();
                    actorDTO.setId(actor.getId());
                    actorDTO.setName(actor.getName());
                    return actorDTO;
                })
                .collect(Collectors.toList());
        movieDTO.setActors(actorDTOs);

        return movieDTO;
    }


    private Movie convertToEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setId(movieDTO.getId());
        movie.setTitle(movieDTO.getTitle());
        movie.setReleaseYear(movieDTO.getReleaseYear());

        if (movieDTO.getGenres() != null) {
            List<Genre> genres = movieDTO.getGenres().stream()
                    .map(genreDTO -> genreService.findGenreById(genreDTO.getId()))
                    .collect(Collectors.toList());
            movie.setGenres(genres);
        }

        if (movieDTO.getActors() != null) {
            List<Actor> actors = movieDTO.getActors().stream()
                    .map(actorDTO -> actorService.findActorById(actorDTO.getId()))
                    .collect(Collectors.toList());
            movie.setActors(actors);
        }

        return movie;
    }
}