package com.example.crudspringular.service;

import com.example.crudspringular.dto.ActorDTO;
import com.example.crudspringular.dto.GenreDTO;
import com.example.crudspringular.dto.MovieDTO;
import com.example.crudspringular.entity.Movie;
import com.example.crudspringular.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    public List<MovieDTO> findAllMovies() {
        log.info("Attempting to fetch all movies");
        List<MovieDTO> movies = movieRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        log.info("Fetched {} movies", movies.size());
        return movies;
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


        Set<GenreDTO> genreDTOs = movie.getGenres().stream()
                .map(genreService::convertToDto)
                .collect(Collectors.toSet());
        movieDTO.setGenres(genreDTOs); // Now this should work since MovieDTO expects a Set<GenreDTO>

        // Since actors is already a Set<ActorDTO>, we keep it as is
        Set<ActorDTO> actorDTOs = movie.getActors().stream()
                .map(actorService::convertToDto)
                .collect(Collectors.toSet());
        movieDTO.setActors(actorDTOs);

        // ... map other fields as necessary

        return movieDTO;
    }

    private Movie convertToEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setId(movieDTO.getId());
        movie.setTitle(movieDTO.getTitle());
        movie.setReleaseYear(movieDTO.getReleaseYear());


        // Map other fields as necessary
        return movie;
    }
}