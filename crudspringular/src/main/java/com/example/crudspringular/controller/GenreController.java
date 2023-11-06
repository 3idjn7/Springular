package com.example.crudspringular.controller;

import com.example.crudspringular.entity.Genre;
import com.example.crudspringular.service.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        log.info("Received request to create genre: {}", genre.getName());
        Genre savedGenre = genreService.saveGenre(genre);
        log.info("Created genre with ID: {}", savedGenre.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGenre);
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        log.info("Received request to list all genres");
        List<Genre> genres = genreService.findAllGenres();
        log.info("Returning {} genres", genres.size());
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        log.info("Received request to get genre with ID: {}", id);
        Optional<Genre> genre = genreService.findGenreById(id);
        if (genre.isPresent()) {
            log.info("Returning genre with ID: {}", id);
            return ResponseEntity.ok(genre.get());
        } else {
            log.info("Genre with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/findOrCreate")
    public ResponseEntity<Genre> findOrCreateGenre(@RequestBody String name) {
        log.info("Received request to find or create genre with name: {}", name);
        Genre genre = genreService.findOrCreateByName(name);
        log.info("Returning genre: {}", genre.getName());
        return ResponseEntity.ok(genre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.info("Received request to delete genre with ID: {}", id);
        genreService.deleteGenre(id);
        log.info("Deleted genre with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
