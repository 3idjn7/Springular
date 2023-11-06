package com.example.crudspringular.service;

import com.example.crudspringular.dto.MovieDto;
import com.example.crudspringular.entity.Actor;
import com.example.crudspringular.entity.Genre;
import com.example.crudspringular.entity.Movie;
import com.example.crudspringular.repository.ActorRepository;
import com.example.crudspringular.repository.GenreRepository;
import com.example.crudspringular.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;

    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository, ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
    }

    public Movie createMovieFromDto(MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setReleaseYear(movieDto.getReleaseYear());

        // Handling multiple genres
        Set<Genre> genres = new HashSet<>();
        if (movieDto.getGenreIds() != null && !movieDto.getGenreIds().isEmpty()) {
            genres.addAll(genreRepository.findAllById(movieDto.getGenreIds()));
            if (genres.size() != movieDto.getGenreIds().size()) {
                throw new IllegalArgumentException("One or more Genre IDs are invalid");
            }
        } else {
            throw new IllegalArgumentException("Genre IDs must not be null or empty");
        }
        movie.setGenre(genres);

        // Handling actors the same way as before
        Set<Long> actorIds = movieDto.getActorIds();
        if (actorIds == null || actorIds.isEmpty()) {
            throw new IllegalArgumentException("Actor IDs must not be null or empty");
        }
        Set<Actor> actors = new HashSet<>(actorRepository.findAllById(actorIds));
        if (actors.size() != actorIds.size()) {
            throw new IllegalArgumentException("One or more Actor IDs are invalid");
        }
        movie.setActors(actors);

        return movie;
    }

    @Transactional
    public Movie updateMovieFromDto(Movie movie, MovieDto movieDto) {
        log.info("Updating movie with ID: {}", movie.getId());

        movie.setTitle(movieDto.getTitle());
        movie.setReleaseYear(movieDto.getReleaseYear());

        // Update genres if necessary
        if (movieDto.getGenreIds() != null && !movieDto.getGenreIds().isEmpty()) {
            Set<Genre> updatedGenres = new HashSet<>(genreRepository.findAllById(movieDto.getGenreIds()));
            if (updatedGenres.size() != movieDto.getGenreIds().size()) {
                throw new IllegalArgumentException("One or more Genre IDs are invalid");
            }
            movie.setGenre(updatedGenres);
        } else {
            throw new IllegalArgumentException("Genre IDs must not be null or empty");
        }

        // Update actors if necessary
        if (movieDto.getActorIds() != null && !movieDto.getActorIds().isEmpty()) {
            Set<Actor> updatedActors = new HashSet<>(actorRepository.findAllById(movieDto.getActorIds()));
            if (updatedActors.size() != movieDto.getActorIds().size()) {
                throw new IllegalArgumentException("One or more Actor IDs are invalid");
            }
            movie.setActors(updatedActors);
        } else {
            throw new IllegalArgumentException("Actor IDs must not be null or empty");
        }

        log.info("Updated movie with ID: {}", movie.getId());
        return movie;
    }


    @Transactional
    public Movie saveMovie(Movie movie) {
        log.info("Attempting to save movie: {}", movie.getTitle());
        Movie savedMovie = movieRepository.save(movie);
        log.info("Saved movie: {}", savedMovie.getTitle());
        return savedMovie;
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
