package com.example.crudspringular.controller;

import com.example.crudspringular.dto.GenreDTO;
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
    public ResponseEntity<GenreDTO> createGenre(@RequestBody GenreDTO genreDTO) {
        log.info("Received request to create genre: {}", genreDTO.getName());
        GenreDTO savedGenreDTO = genreService.saveGenre(genreDTO);
        log.info("Created genre with ID: {}", savedGenreDTO.getId());
        return new ResponseEntity<>(savedGenreDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        log.info("Received request to list all genres");
        List<GenreDTO> genreDTOs = genreService.findAllGenres();
        log.info("Returning {} genres", genreDTOs.size());
        return ResponseEntity.ok(genreDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable Long id) {
        log.info("Received request to get genre with ID: {}", id);
        try {
            Genre genre = genreService.findGenreById(id);
            GenreDTO genreDTO = genreService.convertToDto(genre);
            return ResponseEntity.ok(genreDTO);
        } catch (RuntimeException e) {
            log.info("Genre with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.info("Received request to delete genre with ID: {}", id);
        genreService.deleteGenre(id);
        log.info("Deleted genre with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
