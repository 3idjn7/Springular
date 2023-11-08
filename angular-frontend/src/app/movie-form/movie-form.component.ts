import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
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
    private location: Location,
    private route: ActivatedRoute,
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
    this.checkEditMode();
  }
  // Access method to fetch the actors to the form page
  getActors(): void {
    this.actorService.getActors()
      .subscribe(actors => this.actors = actors);
  }
  // Access method to fetch the genres to the form page
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
               console.log(result);
              }); 
            } else {
              this.movieService.addMovie(movieData).subscribe(result => {
              console.log(result);
            });
         }
      }
  }
  // Back button method
  goBack(): void {
    this.location.back();
  }
  
  checkEditMode(): void {
    const movieId = this.route.snapshot.params['id'];
    if (movieId) {
      this.isEdit = true;
    }
  }
}
