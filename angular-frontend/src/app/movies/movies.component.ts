import { Component, OnInit } from '@angular/core';
import { MovieService } from '../services/movie.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-movies',
  templateUrl: './movies.component.html',
  styleUrls: ['./movies.component.css']
})
export class MoviesComponent implements OnInit {

  movies!: any[];

  constructor(
    private movieService: MovieService,
    private router: Router,
  ) { }

  ngOnInit() {
    console.log('MoviesComponent initialized');
    this.reloadData();
  }

    reloadData() {
      console.log('Reloading movie data...');
      this.movieService.getMovies().subscribe(data => {
        // Log raw movie data
        console.log('Data from getMovies:', data);

        const processedMovies = data.map((movie: any) => {
          // Log the raw genre and actors to see their structure for each movie
          console.log('Processing movie:', movie);
          console.log('Raw genre:', movie.genre);
          console.log('Raw actors:', movie.actors);
          const genreNames = movie.genres && movie.genres.length > 0
              ? movie.genres.map((g: { name: any; }) => g.name).join(', ')
              : 'N/A';

          const processedMovie = {
            ...movie,
            genreNames: genreNames,
            actorNames: movie.actors && movie.actors.length > 0
                        ? movie.actors.map((actor: any) => actor.name).join(', ')
                        : 'No actors',
            releaseYear: movie.releaseYear || 'Unknown',
          };

          // Log the processed movie object
          console.log('Processed movie:', processedMovie);

          return processedMovie;
        });

        // Log the fully processed movies array
        console.log('Movies with genres processed:', processedMovies);

        this.movies = processedMovies;
      }, error => console.log('Error fetching movies:', error));
    }



  addMovie(): void {
    this.router.navigate(['/add-movie']);
  }

  addActor(): void {
    this.router.navigate(['/add-actor']);
  }

  addGenre(): void {
    this.router.navigate(['/add-genre']); 
  }

  deleteMovie(id: number) {
    this.movieService.deleteMovie(id)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  updateMovie(id: number) {
    this.router.navigate(['/movie/edit', id]);
  }
}
