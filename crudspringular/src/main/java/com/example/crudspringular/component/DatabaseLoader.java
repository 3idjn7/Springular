//package com.example.crudspringular.component;
//
//import com.example.crudspringular.dto.ActorDTO;
//import com.example.crudspringular.dto.GenreDTO;
//import com.example.crudspringular.dto.MovieDTO;
//import com.example.crudspringular.service.ActorService;
//import com.example.crudspringular.service.GenreService;
//import com.example.crudspringular.service.MovieService;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import java.util.*;
//
//@Component
//public class DatabaseLoader implements CommandLineRunner {
//
//    private final MovieService movieService;
//    private final ActorService actorService;
//    private final GenreService genreService;
//    private final Random random = new Random();
//
//    // Constructor with autowired services
//    public DatabaseLoader(MovieService movieService, ActorService actorService, GenreService genreService) {
//        this.movieService = movieService;
//        this.actorService = actorService;
//        this.genreService = genreService;
//    }
//
//    private String generateRandomTitle() {
//        String[] adjectives = {"Lost", "Eternal", "Dark", "Mystic", "Silent", "Hidden", "Forbidden", "Distant", "Final", "Infinite"};
//        String[] nouns = {"City", "Forest", "Ocean", "Mountain", "Shadow", "Light", "Legend", "Hero", "Voyage", "Destiny"};
//        return adjectives[random.nextInt(adjectives.length)] + " " + nouns[random.nextInt(nouns.length)];
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Add multiple genres
//        String[] genreNames = {"Action", "Drama", "Comedy", "Horror", "Sci-Fi", "Romance", "Thriller", "Fantasy", "Documentary", "Animation"};
//        List<GenreDTO> savedGenres = new ArrayList<>();
//        for (String name : genreNames) {
//            GenreDTO genreDTO = new GenreDTO();
//            genreDTO.setName(name);
//            savedGenres.add(genreService.saveGenre(genreDTO));
//        }
//
//        // Add multiple actors
//        String[] actorNames = {"Tom Cruise", "Will Smith", "Angelina Jolie", "Meryl Streep", "Leonardo DiCaprio", "Brad Pitt", "Johnny Depp", "Sandra Bullock", "Julia Roberts", "Denzel Washington"};
//        List<ActorDTO> savedActors = new ArrayList<>();
//        for (String name : actorNames) {
//            ActorDTO actorDTO = new ActorDTO();
//            actorDTO.setName(name);
//            savedActors.add(actorService.saveActor(actorDTO));
//        }
//
//        // Add 15 movies with random combinations of genres, actors, and titles
//        for (int i = 1; i <= 15; i++) {
//            MovieDTO movieDTO = new MovieDTO();
//            movieDTO.setTitle(generateRandomTitle()); // Use the random title generator
//            movieDTO.setReleaseYear(1990 + random.nextInt(30)); // Random year between 1990 and 2020
//
//            // Randomly assign genres and actors
//            Collections.shuffle(savedGenres);
//            Collections.shuffle(savedActors);
//            List<GenreDTO> movieGenres = savedGenres.subList(0, 2 + random.nextInt(savedGenres.size() - 2));
//            List<ActorDTO> movieActors = savedActors.subList(0, 1 + random.nextInt(savedActors.size() - 1));
//
//            movieDTO.setGenres(new ArrayList<>(movieGenres));
//            movieDTO.setActors(new ArrayList<>(movieActors));
//            movieService.saveMovie(movieDTO);
//        }
//    }
//}