package com.example.crudspringular.service.unit;

import com.example.crudspringular.dto.MovieDTO;
import com.example.crudspringular.entity.Movie;
import com.example.crudspringular.repository.MovieRepository;
import com.example.crudspringular.service.ActorService;
import com.example.crudspringular.service.GenreService;
import com.example.crudspringular.service.MovieService;
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
    void testSaveExistingMovieThrowsException() {
        when(movieRepository.findByTitleAndReleaseYear(movieDTO.getTitle(), movieDTO.getReleaseYear()))
                .thenReturn(Optional.of(movie));
        assertThrows(IllegalStateException.class, () -> movieService.saveMovie(movieDTO));
        verify(movieRepository).findByTitleAndReleaseYear(movieDTO.getTitle(), movieDTO.getReleaseYear());
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testSaveNullMovieDTO() {
        assertThrows(NullPointerException.class, () -> movieService.saveMovie(null));
    }

    @Test
    void testUpdateMovie() {
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        // Set valid title and release year
        movieDTO.setTitle("Valid Title");
        movieDTO.setReleaseYear(2020); // Use a valid year

        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        MovieDTO updatedMovieDTO = movieService.updateMovie(movieId, movieDTO);

        assertNotNull(updatedMovieDTO);
        verify(movieRepository).findById(movieId);
        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void testUpdateNonExistingMovieThrowsException() {
        Long nonExistingMovieId = 999L;
        when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

        // Set valid title and release year
        movieDTO.setTitle("Valid Title");
        movieDTO.setReleaseYear(2020);

        assertThrows(RuntimeException.class, () -> movieService.updateMovie(nonExistingMovieId, movieDTO));

        verify(movieRepository).findById(nonExistingMovieId);
    }

    @Test
    void testUpdateMovieWithInvalidFields() {
        Long existingMovieId = 1L;

        // Set invalid fields in movieDTO (e.g., null title)
        movieDTO.setTitle(null);
        movieDTO.setReleaseYear(2101); // or any invalid release year

        assertThrows(IllegalArgumentException.class, () -> movieService.updateMovie(existingMovieId, movieDTO));

        verify(movieRepository, never()).findById(anyLong());
        verify(movieRepository, never()).save(any(Movie.class));
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
    void testSearchMoviesWithEmptyOrNullSearchTerm() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);

        when(movieRepository.findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(anyString(), any(PageRequest.class)))
                .thenReturn(Page.empty());

        // Test with an empty string
        Page<MovieDTO> resultEmpty = movieService.searchMovies("", page, size);
        assertNotNull(resultEmpty);
        assertTrue(resultEmpty.isEmpty());

        // Test with null
        Page<MovieDTO> resultNull = movieService.searchMovies(null, page, size);
        assertNotNull(resultNull);
        assertTrue(resultNull.isEmpty());

        verify(movieRepository, times(1)).findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(eq(""), eq(pageRequest));
        verify(movieRepository, times(1)).findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(eq(null), eq(pageRequest));
    }

    @Test
    void testSearchMoviesNoMatch() {
        String searchTerm = "nonexistent";
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);

        when(movieRepository.findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(eq(searchTerm), eq(pageRequest)))
                .thenReturn(Page.empty());

        Page<MovieDTO> result = movieService.searchMovies(searchTerm, page, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());

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
    void testFindAllMoviesWithDifferentPageable() {
        int[] pageNumbers = {0, 1, 2}; // Different page numbers
        int[] pageSizes = {5, 10, 20}; // Different page sizes

        for (int pageNumber : pageNumbers) {
            for (int pageSize : pageSizes) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize);
                when(movieRepository.findAll(pageable)).thenReturn(Page.empty());

                Page<MovieDTO> result = movieService.findAllMovies(pageable);

                assertNotNull(result);
                verify(movieRepository).findAll(pageable);
            }
        }
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
    void testFindMovieByIdWhenNoMovieExists() {
        Long nonExistingMovieId = 999L; // An ID assumed not to exist
        when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

        Optional<MovieDTO> result = movieService.findMovieById(nonExistingMovieId);

        assertFalse(result.isPresent());
        verify(movieRepository).findById(nonExistingMovieId);
    }

    @Test
    void testDeleteMovie() {
        Long movieId = 1L;
        movieService.deleteMovie(movieId);

        verify(movieRepository).deleteById(movieId);
    }

    @Test
    void testDeleteNonExistingMovie() {
        Long nonExistingMovieId = 999L; // An ID assumed not to exist
        doNothing().when(movieRepository).deleteById(nonExistingMovieId);

        movieService.deleteMovie(nonExistingMovieId);

        verify(movieRepository).deleteById(nonExistingMovieId);
    }

    @Test
    void testDeleteMovieDoesNotAffectRelatedEntities() {
        Long movieId = 1L;
        doNothing().when(movieRepository).deleteById(movieId);

        movieService.deleteMovie(movieId);

        verify(movieRepository).deleteById(movieId);
        // Verify that no methods on genreService or actorService are called
        verifyNoInteractions(genreService);
        verifyNoInteractions(actorService);
    }
}
