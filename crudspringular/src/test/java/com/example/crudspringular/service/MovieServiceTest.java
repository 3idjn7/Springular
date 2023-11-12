package com.example.crudspringular.service;

import com.example.crudspringular.dto.MovieDTO;
import com.example.crudspringular.entity.Movie;
import com.example.crudspringular.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private GenreService genreService;

    @Mock
    private ActorService actorService;

    @InjectMocks
    private MovieService movieService;

    private MovieDTO movieDTO;
    private Movie movie;

    @BeforeEach
    void setUp() {
        // Initialize your MovieDTO and Movie objects here
        movieDTO = new MovieDTO();
        // Set properties for movieDTO as required

        movie = new Movie();
        // Set properties for movie as required


    }

    @Test
    void testSaveMovie() {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        MovieDTO savedMovieDTO = movieService.saveMovie(movieDTO);

        assertNotNull(savedMovieDTO);
        assertEquals(movie.getTitle(), savedMovieDTO.getTitle());
        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void testUpdateMovie() {
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        MovieDTO updatedMovieDTO = movieService.updateMovie(movieId, movieDTO);

        assertNotNull(updatedMovieDTO);
        verify(movieRepository).findById(movieId);
        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void testSearchMovies() {
        String searchTerm = "test";
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        when(movieRepository.findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(eq(searchTerm), eq(pageRequest)))
                .thenReturn(Page.empty());

        Page<MovieDTO> result = movieService.searchMovies(searchTerm, page, size);

        assertNotNull(result);
        verify(movieRepository).findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(eq(searchTerm), eq(pageRequest));
    }

    @Test
    void testFindAllMovies() {
        Pageable pageable = PageRequest.of(0, 10);
        when(movieRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<MovieDTO> result = movieService.findAllMovies(pageable);

        assertNotNull(result);
        verify(movieRepository).findAll(pageable);
    }

    @Test
    void testFindMovieById() {
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        Optional<MovieDTO> result = movieService.findMovieById(movieId);

        assertTrue(result.isPresent());
        verify(movieRepository).findById(movieId);
    }

    @Test
    void testDeleteMovie() {
        Long movieId = 1L;
        movieService.deleteMovie(movieId);

        verify(movieRepository).deleteById(movieId);
    }
}
