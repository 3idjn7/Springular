package com.example.crudspringular.controller;

import com.example.crudspringular.dto.MovieDTO;
import com.example.crudspringular.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO movieDTO) {
        log.info("Received request to create movie: {}", movieDTO.getTitle());
        MovieDTO savedMovieDTO = movieService.saveMovie(movieDTO);
        log.info("Created movie with ID: {}", savedMovieDTO.getId());
        return new ResponseEntity<>(savedMovieDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long id, @RequestBody MovieDTO movieDTO) {
        log.info("Received request to update movie with ID: {}", id);
        MovieDTO updatedMovieDTO = movieService.updateMovie(id, movieDTO);
        log.info("Updated movie with ID: {}", updatedMovieDTO.getId());
        return ResponseEntity.ok(updatedMovieDTO);
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
    public ResponseEntity<Page<MovieDTO>> searchMovies(
            @RequestParam(value = "query") String query, // Changed to match the frontend
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(movieService.searchMovies(query, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.info("Received request to delete movie with ID: {}", id);
        movieService.deleteMovie(id);
        log.info("Deleted movie with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
