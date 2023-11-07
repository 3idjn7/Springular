import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MovieService } from '../services/movie.service';
import { Actor } from '../models/actor.model';
import { Genre } from '../models/genre.model';
import { ActorService } from '../services/actor.service';
import { GenreService } from '../services/genre.service';

@Component({
  selector: 'app-movie-form',
  templateUrl: './movie-form.component.html',
  styleUrls: ['./movie-form.component.css']
})
export class MovieFormComponent implements OnInit {
  movieForm: FormGroup;
  actors: Actor[] = [];
  genres: Genre[] = [];
  isEdit = false;

  constructor(
    private fb: FormBuilder,
    private movieService: MovieService,
    private actorService: ActorService,
    private genreService: GenreService,
  ) {
    this.movieForm = this.fb.group({
      title: ['', Validators.required],
      director: ['', Validators.required],
      releaseYear: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]],
      actors: [[]],
      genres: [[]],
    });
  }

  ngOnInit() {
    this.getActors();
    this.getGenres();
  }

  getActors(): void {
    this.actorService.getActors()
      .subscribe(actors => this.actors = actors);
  }

  getGenres(): void {
    this.genreService.getGenres()
      .subscribe(genres => this.genres = genres);
  }

  onSubmit(): void {
    if (this.movieForm.valid) {
      let movieData = this.movieForm.value;
      movieData.releaseYear = parseInt(movieData.releaseYear, 10);
      console.log('Selected genres:', movieData.genres);
    if (this.isEdit) {
      this.movieService.updateMovie(movieData.id, movieData).subscribe(result => {
        // Handle result
        console.log(result);
      });
    } else {
      this.movieService.addMovie(movieData).subscribe(result => {
        // Handle result
        console.log(result);
      });
    }
  }
}


  // More methods for form actions if needed
}
