package com.example.crudspringular.controller;

import com.example.crudspringular.dto.MovieDTO;
import com.example.crudspringular.service.MovieService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        log.info("Received request to create movie: {}", movieDTO.getTitle());
        MovieDTO savedMovieDTO = movieService.saveMovie(movieDTO);
        log.info("Created movie with ID: {}", savedMovieDTO.getId());
        return new ResponseEntity<>(savedMovieDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieDTO movieDTO) {
        log.info("Received request to update movie with ID: {}", id);

        // Check for positive ID
        if (id <= 0) {
            return ResponseEntity.badRequest().body("Invalid ID: ID must be positive.");
        }

        try {
            MovieDTO updatedMovieDTO = movieService.updateMovie(id, movieDTO);
            log.info("Updated movie with ID: {}", updatedMovieDTO.getId());
            return ResponseEntity.ok(updatedMovieDTO);
        } catch (EntityNotFoundException e) {
            log.info("Movie with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<MovieDTO>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<MovieDTO> movieDTOs = movieService.findAllMovies(pageRequest);
        log.info("Returning {} movies", movieDTOs.getTotalElements());
        return ResponseEntity.ok(movieDTOs);
    }

    @GetMapping("/{id:[\\d]+}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        log.info("Received request to get movie with ID: {}", id);
        Optional<MovieDTO> movieDTO = movieService.findMovieById(id);
        return movieDTO.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.info("Movie with ID: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(
            @RequestParam(value = "query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Sanitize and validate the query
        String sanitizedQuery = sanitizeInput(query);
        if (sanitizedQuery.length() > MAX_QUERY_LENGTH) {
            return ResponseEntity.badRequest().body("Query too long");
        }

        // Validate pagination parameters
        if (page < 0 || size <= 0 || size > MAX_PAGE_SIZE) {
            return ResponseEntity.badRequest().body("Invalid pagination parameters");
        }

        return ResponseEntity.ok(movieService.searchMovies(sanitizedQuery, page, size));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.info("Received request to delete movie with ID: {}", id);
        movieService.deleteMovie(id);
        log.info("Deleted movie with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }

        // Trim the input to remove leading and trailing whitespace
        String trimmedInput = input.trim();

        // Replace special characters with their escaped equivalents
        return trimmedInput.replaceAll("[<>'\"/]", "");
    }

    private static final int MAX_QUERY_LENGTH = 255;
    private static final int MAX_PAGE_SIZE = 50;
}
