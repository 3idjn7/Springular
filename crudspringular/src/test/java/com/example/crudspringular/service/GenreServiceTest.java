package com.example.crudspringular.service;

import com.example.crudspringular.dto.GenreDTO;
import com.example.crudspringular.entity.Genre;
import com.example.crudspringular.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    private GenreDTO genreDTO;
    private Genre genre;

    @BeforeEach
    void setUp() {
        genreDTO = new GenreDTO();
        genreDTO.setId(1L);
        genreDTO.setName("Action");

        genre = new Genre();
        genre.setId(genreDTO.getId());
        genre.setName(genreDTO.getName());
    }

    @Test
    void testSaveGenre() {
        when(genreRepository.save(any(Genre.class))).thenReturn(genre);

        GenreDTO savedGenreDTO = genreService.saveGenre(genreDTO);

        assertNotNull(savedGenreDTO);
        assertEquals(genre.getName(), savedGenreDTO.getName());
        verify(genreRepository).save(any(Genre.class));
    }

    @Test
    void testFindAllGenres() {
        when(genreRepository.findAll()).thenReturn(Arrays.asList(genre));

        List<GenreDTO> genres = genreService.findAllGenres();

        assertNotNull(genres);
        assertFalse(genres.isEmpty());
        assertEquals(1, genres.size());
        verify(genreRepository).findAll();
    }

    @Test
    void testFindGenreById() {
        Long genreId = 1L;
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));

        Genre foundGenre = genreService.findGenreById(genreId);

        assertNotNull(foundGenre);
        assertEquals(genre.getId(), foundGenre.getId());
        verify(genreRepository).findById(genreId);
    }

    @Test
    void testDeleteGenre() {
        Long genreId = 1L;
        genreService.deleteGenre(genreId);

        verify(genreRepository).deleteById(genreId);
    }
}
