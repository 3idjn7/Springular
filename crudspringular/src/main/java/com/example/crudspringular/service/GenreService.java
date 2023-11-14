package com.example.crudspringular.service;

import com.example.crudspringular.dto.GenreDTO;
import com.example.crudspringular.entity.Genre;
import com.example.crudspringular.repository.GenreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public GenreDTO saveGenre(GenreDTO genreDTO) {
        log.info("Attempting to save genre: {}", genreDTO.getName());
        if (genreRepository.findByName(genreDTO.getName()).isPresent()) {
            throw new IllegalStateException("Genre with name '" + genreDTO.getName() + "' already exists");
        }
        Genre genre = convertToEntity(genreDTO);
        Genre savedGenre = genreRepository.save(genre);
        GenreDTO savedGenreDTO = convertToDto(savedGenre);
        log.info("Saved genre: {}", savedGenreDTO.getName());
        return savedGenreDTO;
    }

    public List<GenreDTO> findAllGenres() {
        log.info("Attempting to fetch all genres");
        List<GenreDTO> genres = genreRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        log.info("Fetched {} genres", genres.size());
        return genres;
    }

    public Genre findGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
    }

    public void deleteGenre(Long id) {
        log.info("Attempting to delete genre with ID: {}", id);
        genreRepository.deleteById(id);
        log.info("Deleted genre with ID: {}", id);
    }

    public GenreDTO convertToDto(Genre genre) {
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(genre.getId());
        genreDTO.setName(genre.getName());
        return genreDTO;
    }

    private Genre convertToEntity(GenreDTO genreDTO) {
        Genre genre = new Genre();
        genre.setId(genreDTO.getId());
        genre.setName(genreDTO.getName());
        return genre;
    }
}