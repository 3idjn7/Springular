import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MovieService } from '../../services/movie.service';
import { Actor } from '../../models/actor.model';
import { Genre } from '../../models/genre.model';
import { ActorService } from '../../services/actor.service';
import { GenreService } from '../../services/genre.service';

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
  currentPage = 0;

  constructor(
    private fb: FormBuilder,
    private movieService: MovieService,
    private actorService: ActorService,
    private genreService: GenreService,
    private router: Router,
    private location: Location,
    private route: ActivatedRoute,
  ) {
    // Initialize the movieForm FormGroup
    this.movieForm = this.fb.group({
      title: ['', Validators.required],
      director: ['', Validators.required],
      releaseYear: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]],
      actors: [[]],
      genres: [[]],
    });
  }

  ngOnInit() {
    // Get the current page from query params
    this.route.queryParams.subscribe(params => {
      this.currentPage = params['page'] ? parseInt(params['page'], 10) : 0;
    });

    // Fetch actors and genres
    this.getActors();
    this.getGenres();

    // Check if the form is in edit mode
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
        // Obtain the ID from the route parameters and ensure it is a number.
        const movieId = this.route.snapshot.params['id'];
        if (movieId) {
          this.movieService.updateMovie(movieId, movieData).subscribe(result => {
            console.log('Movie updated:', result);
            // Add navigation or success message here if needed
          }, error => {
            console.error('Error updating movie:', error);
          });
        } else {
          console.error('Movie ID is undefined');
        }
      } else {
        this.movieService.addMovie(movieData).subscribe(result => {
          console.log('Movie added:', result);
          // Add navigation or success message here if needed
        }, error => {
          console.error('Error adding movie:', error);
        });
      }
    }
  }

  // Back button method
  goBack(): void {
    this.router.navigate(['/movies'], { queryParams: { page: this.currentPage } });
  }

  checkEditMode(): void {
    const movieId = this.route.snapshot.params['id'];
    if (movieId && movieId !== 'undefined') {
      this.isEdit = true;
      this.movieService.getMovie(movieId).subscribe(movie => {
        this.movieForm.patchValue(movie);
      });
    } else {
      console.log('error at checkEditMode');
    }
  }
}
