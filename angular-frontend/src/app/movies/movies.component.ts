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
    this.reloadData();
  }

    reloadData() {
      this.movieService.getMovies().subscribe(data => {
        console.log('Raw movie data:', data);
        this.movies = data.map((movie: any) => {
          // Log the raw genre and actors to see their structure
          console.log('Raw genre:', movie.genre);
          console.log('Raw actors:', movie.actors);
          
          return {
            ...movie,
            // Since genre is not an array, we access its name directly
            genreName: movie.genre ? movie.genre.name : 'N/A', 
            // We map the actors if they exist, otherwise provide an empty string
            actorNames: movie.actors && movie.actors.length > 0
                        ? movie.actors.map((actor: any) => actor.name).join(', ')
                        : 'No actors', // Placeholder for no actors
            // Director is not part of the data as per your current DTO
            // directorName: movie.director ? movie.director.name : 'N/A' // If director existed
          };
        });
      }, error => console.log(error));
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
