package com.example.crudspringular.service.integration;

import com.example.crudspringular.dto.ActorDTO;
import com.example.crudspringular.dto.GenreDTO;
import com.example.crudspringular.dto.MovieDTO;
import com.example.crudspringular.repository.MovieRepository;
import com.example.crudspringular.service.ActorService;
import com.example.crudspringular.service.GenreService;
import com.example.crudspringular.service.MovieService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest
@Transactional
public class MovieServiceIntegrationTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreService genreService;

    @Autowired
    private ActorService actorService;

    private List<GenreDTO> existingGenres;
    private List<ActorDTO> existingActors;

    @BeforeEach
    void setUp() {
        // Fetch existing genres and actors
        existingGenres = genreService.findAllGenres();
        existingActors = actorService.findAllActors();
    }

    @Test
    public void testSaveAndRetrieveMovie() {
        // Create a new MovieDTO with required properties
        MovieDTO newMovie = new MovieDTO();
        newMovie.setTitle("Inception");
        newMovie.setReleaseYear(2010);

        // Use existing genres and actors
        if (!existingGenres.isEmpty() && !existingActors.isEmpty()) {
            newMovie.setGenres(Arrays.asList(existingGenres.get(0)));
            newMovie.setActors(Arrays.asList(existingActors.get(0)));

            // Save the movie
            MovieDTO savedMovie = movieService.saveMovie(newMovie);

            // Retrieve the saved movie
            Optional<MovieDTO> retrievedMovie = movieService.findMovieById(savedMovie.getId());

            // Assertions
            assertTrue(retrievedMovie.isPresent());
            assertEquals(savedMovie.getTitle(), retrievedMovie.get().getTitle());
            // Additional assertions can be made to check genres, actors, and release year
        } else {
            fail("No existing genres or actors found in the database.");
        }
    }

    @Test
    public void testUpdateMovieWithNonExistentGenresOrActors() {
        // Assume this is a valid movie ID in your database
        Long existingMovieId = 1L; // Replace with an actual ID

        // Create a MovieDTO with non-existent genres and actors
        MovieDTO movieToUpdate = new MovieDTO();
        movieToUpdate.setTitle("Updated Title");
        movieToUpdate.setReleaseYear(2021);

        // Create and set non-existent genre
        GenreDTO nonExistentGenre = new GenreDTO();
        nonExistentGenre.setId(Long.MAX_VALUE); // Use a non-existent ID
        nonExistentGenre.setName("NonExistentGenre");
        movieToUpdate.setGenres(Arrays.asList(nonExistentGenre));

        // Create and set non-existent actor
        ActorDTO nonExistentActor = new ActorDTO();
        nonExistentActor.setId(Long.MAX_VALUE); // Use a non-existent ID
        nonExistentActor.setName("NonExistentActor");
        movieToUpdate.setActors(Arrays.asList(nonExistentActor));

        // Attempt to update the movie
        try {
            MovieDTO updatedMovie = movieService.updateMovie(existingMovieId, movieToUpdate);
            fail("Expected an exception to be thrown for non-existent genres or actors.");
        } catch (Exception e) {
            // Assert that an appropriate exception is thrown
            assertTrue(e instanceof RuntimeException); // Replace with the specific exception you expect
            // Additional assertions can be made on the exception message, if needed
        }
    }

    @Test
    public void testDeleteMovieAndVerifyCascadeEffects() {
        // Assume this is an existing movie ID
        Long movieIdToDelete = 5L; // Replace with an actual movie ID

        // Fetch the movie along with its genres and actors before deletion
        Optional<MovieDTO> movieBeforeDeletion = movieService.findMovieById(movieIdToDelete);
        assertTrue(movieBeforeDeletion.isPresent(), "Movie should exist before deletion");

        List<GenreDTO> genresBeforeDeletion = new ArrayList<>(movieBeforeDeletion.get().getGenres());
        List<ActorDTO> actorsBeforeDeletion = new ArrayList<>(movieBeforeDeletion.get().getActors());

        // Delete the movie
        movieService.deleteMovie(movieIdToDelete);

        // Verify the movie is deleted
        Optional<MovieDTO> movieAfterDeletion = movieService.findMovieById(movieIdToDelete);
        assertFalse(movieAfterDeletion.isPresent(), "Movie should not exist after deletion");

        // Verify if genres and actors are still present in the database
        genresBeforeDeletion.forEach(genreDTO -> {
            assertNotNull(genreService.findGenreById(genreDTO.getId()),
                    "Genre should still exist after movie deletion");
        });

        actorsBeforeDeletion.forEach(actorDTO -> {
            assertNotNull(actorService.findActorById(actorDTO.getId()),
                    "Actor should still exist after movie deletion");
        });

        // Additional verifications can be made depending on the cascade behavior set
    }

    @Test
    public void testSearchMoviesWithVariousFilters() {
        String knownActor = "Tom Cruise";
        Page<MovieDTO> resultsByKnownActor = movieService.searchMovies(knownActor, 0, 10);
        assertFalse(resultsByKnownActor.isEmpty(), "Should find movies for a known actor");


        String unlikelySearchTerm = "qwertyuiopasdfghjkl";
        Page<MovieDTO> resultsByUnlikelyTerm = movieService.searchMovies(unlikelySearchTerm, 0, 10);
        assertTrue(resultsByUnlikelyTerm.isEmpty(), "Should not find movies for a random string");
    }

}

