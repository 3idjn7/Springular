import { Component, OnInit } from '@angular/core';
import { MovieService } from '../services/movie.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-movies',
  templateUrl: './movies.component.html',
  styleUrls: ['./movies.component.css']
})
export class MoviesComponent implements OnInit {

  movies: any;

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
        this.movies = data.map((movie: any) => ({
      ...movie,
      genreNames: movie.genre?.map((genre: any) => JSON.parse(genre.name).name).join(', '),
      actorNames: movie.actors?.map((actor: any) => JSON.parse(actor.name).name).join(', '),
    
      directorName: movie.director ? JSON.parse(movie.director.name).name : undefined
    }));

  }, error => console.log(error));
}


  addMovie(): void {
    this.router.navigate(['/add-movie']);
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
