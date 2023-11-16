package com.example.crudspringular.repository;

import com.example.crudspringular.entity.Actor;
import com.example.crudspringular.entity.Genre;
import com.example.crudspringular.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        // Save an Actor
        Actor actor = new Actor();
        actor.setName("John Doe");
        actor = actorRepository.save(actor);

        // Save a Genre
        Genre genre = new Genre();
        genre.setName("Action");
        genre = genreRepository.save(genre);

        // Save multiple Movies for pagination and sorting tests
        for (int i = 1; i <= 10; i++) {
            Movie movie = new Movie();
            movie.setTitle("Test Movie " + i);
            movie.setReleaseYear(2000 + i); // Different release years
            movie.getActors().add(actor);
            movie.getGenres().add(genre);
            movieRepository.save(movie);
        }
    }

    @Test
    void testFindByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String searchTerm = "test";

        Page<Movie> result = movieRepository.findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(searchTerm, pageRequest);

        assertFalse(result.isEmpty());
        assertTrue(result.getContent().stream()
                .anyMatch(m -> m.getTitle().equalsIgnoreCase("Test Movie 1")));
    }

    @Test
    void testPagination() {
        int pageNumber = 0;
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<Movie> page = movieRepository.findAll(pageRequest);

        assertEquals(pageSize, page.getContent().size());
        assertEquals(25, page.getTotalElements());
    }

    @Test
    void testSortingByTitle() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("title"));

        Page<Movie> page = movieRepository.findAll(pageRequest);
        List<Movie> movies = page.getContent();

        assertTrue(isSortedByTitle(movies));
    }

    @Test
    void testSortingByReleaseYear() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("releaseYear"));

        Page<Movie> page = movieRepository.findAll(pageRequest);
        List<Movie> movies = page.getContent();

        assertTrue(isSortedByReleaseYear(movies));
    }

    @Test
    void testSearchByMatchingTitle() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String searchTerm = "Movie 1"; // Assuming "Test Movie 1" exists

        Page<Movie> result = movieRepository.findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(searchTerm, pageRequest);

        assertFalse(result.isEmpty());
        assertTrue(result.getContent().stream()
                .anyMatch(m -> m.getTitle().equalsIgnoreCase("Test Movie 1")));
    }

    @Test
    void testSearchByMatchingActorName() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String searchTerm = "John Doe"; // Assuming an actor with this name exists

        Page<Movie> result = movieRepository.findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(searchTerm, pageRequest);

        assertFalse(result.isEmpty());
        assertTrue(result.getContent().stream()
                .anyMatch(m -> m.getActors().stream()
                        .anyMatch(a -> a.getName().equalsIgnoreCase("John Doe"))));
    }

    @Test
    void testSearchWithNoResults() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String searchTerm = "Nonexistent"; // Term that does not match any movie or actor

        Page<Movie> result = movieRepository.findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(searchTerm, pageRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllMoviesWhenNoneExist() {
        movieRepository.deleteAll(); // Ensure the repository is empty

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Movie> result = movieRepository.findAll(pageRequest);

        assertTrue(result.getContent().isEmpty()); // Expecting an empty result
        assertEquals(0, result.getTotalElements()); // Total elements should be zero
    }

    @Test
    void testSearchMoviesWhenNoneMatch() {
        String searchTerm = "Nonexistent"; // Term that does not match any movie
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Movie> result = movieRepository.findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(searchTerm, pageRequest);

        assertTrue(result.getContent().isEmpty()); // Expecting an empty result
        assertEquals(0, result.getTotalElements()); // Total elements should be zero
    }

    @Test
    void testFindAllWithNegativePageNumber() {
        int invalidPageNumber = -1;
        int pageSize = 10;

        assertThrows(IllegalArgumentException.class, () -> {
            PageRequest pageRequest = PageRequest.of(invalidPageNumber, pageSize);
            movieRepository.findAll(pageRequest);
        });
    }


    @Test
    void testFindAllWithNegativePageSize() {
        int pageNumber = 0;
        int invalidPageSize = -10;

        assertThrows(IllegalArgumentException.class, () -> {
            PageRequest pageRequest = PageRequest.of(pageNumber, invalidPageSize);
            movieRepository.findAll(pageRequest);
        });
    }

    @Test
    void testFindByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCaseWithInvalidPageRequest() {
        String searchTerm = "Movie";
        int invalidPageNumber = 0;
        int invalidPageSize = -10;

        assertThrows(IllegalArgumentException.class, () -> {
            PageRequest pageRequest = PageRequest.of(invalidPageNumber, invalidPageSize);
            movieRepository.findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(searchTerm, pageRequest);
        });
    }


    @Test
    void testDeleteMovie() {
        Actor actor = new Actor();
        actor.setName("Test Actor");
        actor = actorRepository.save(actor);

        Genre genre = new Genre();
        genre.setName("Test Genre");
        genre = genreRepository.save(genre);

        Movie movie = new Movie();
        movie.setTitle("Test Movie for Deletion");
        movie.setReleaseYear(2020);
        movie.getActors().add(actor);
        movie.getGenres().add(genre);
        movie = movieRepository.save(movie);

        Long movieId = movie.getId();

        movieRepository.deleteById(movieId);

        Optional<Movie> deletedMovie = movieRepository.findById(movieId);
        assertFalse(deletedMovie.isPresent());

        assertTrue(actorRepository.findById(actor.getId()).isPresent());
        assertTrue(genreRepository.findById(genre.getId()).isPresent());

        // If there were cascade delete behaviors, can check if related entities are also deleted:
        // assertFalse(actorRepository.findById(actor.getId()).isPresent());
        // assertFalse(genreRepository.findById(genre.getId()).isPresent());
    }

    @Test
    void testFetchingRelationships() {
        // Assuming FetchType is set to LAZY or EAGER
        Actor actor = new Actor();
        actor.setName("Actor 1");
        actorRepository.save(actor);

        Genre genre = new Genre();
        genre.setName("Genre 1");
        genreRepository.save(genre);

        Movie movie = new Movie();
        movie.setTitle("Movie with Relations");
        movie.setReleaseYear(2020);
        movie.getActors().add(actor);
        movie.getGenres().add(genre);
        movieRepository.save(movie);

        // Fetch the movie
        Movie fetchedMovie = movieRepository.findById(movie.getId()).orElseThrow();

        assertFalse(fetchedMovie.getActors().isEmpty());
        assertFalse(fetchedMovie.getGenres().isEmpty());
    }



    private boolean isSortedByTitle(List<Movie> movies) {
        for (int i = 0; i < movies.size() - 1; i++) {
            if (movies.get(i).getTitle().compareTo(movies.get(i + 1).getTitle()) > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isSortedByReleaseYear(List<Movie> movies) {
        for (int i = 0; i < movies.size() - 1; i++) {
            if (movies.get(i).getReleaseYear() > movies.get(i + 1).getReleaseYear()) {
                return false;
            }
        }
        return true;
    }

    // Other tests...
}
