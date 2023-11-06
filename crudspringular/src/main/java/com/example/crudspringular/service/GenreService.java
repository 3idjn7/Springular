package com.example.crudspringular.service;

import com.example.crudspringular.entity.Genre;
import com.example.crudspringular.repository.GenreRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional
    public Genre saveGenre(Genre genre) {
        log.info("Attempting to save genre: {}", genre.getName());
        Genre savedGenre = genreRepository.save(genre);
        log.info("Saved genre: {}", savedGenre.getName());
        return savedGenre;
    }

    public List<Genre> findAllGenres() {
        log.info("Attempting to fetch all genres");
        List<Genre> genres = genreRepository.findAll();
        log.info("Fetched {} genres", genres.size());
        return genres;
    }

    public Optional<Genre> findGenreById(Long id) {
        log.info("Attempting to fetch all genres");
        Optional<Genre> genre = genreRepository.findById(id);
        log.info("Found genre with ID {}: {}", id, genre.isPresent() ? "present" : "not present");
        return genre;
    }
    @PostMapping("/findOrCreate")
    public Genre findOrCreateByName(String name) {
        log.info("Attempting to find or create genre with name: {}", name);
        return genreRepository.findByName(name)
                .orElseGet(() -> {
                    log.info("Actor not found, creating new genre with name: {}", name);
                    Genre newGenre = new Genre();
                    newGenre.setName(name);
                    return genreRepository.save(newGenre);
                });
    }

    @Transactional
    public void deleteGenre(Long id) {
        log.info("Attempting to delete genre with ID: {}", id);
        genreRepository.deleteById(id);
        log.info("Deleted genre with ID: {}", id);
    }
}
