import { Component, OnInit, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { GenreService } from '../../services/genre.service';
import { Genre } from '../../models/genre.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-genres',
  templateUrl: './genres.component.html',
  styleUrls: ['./genres.component.css'],
})
export class GenresComponent implements OnInit, OnDestroy {
  genres: Genre[] = [];
  newGenre: string = '';
  errorMessage: string = '';
  private unsubscribe$ = new Subject<void>();

  constructor(private genreService: GenreService, private location: Location) {}

  ngOnInit() {
    // Initialize component
    this.getGenres();
  }

  ngOnDestroy() {
    // Unsubscribe from observables to prevent memory leaks
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  // Retrieve genres when the component initializes
  getGenres(): void {
    console.log('Fetching genres...');
    this.genreService
      .getGenres()
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((genres) => {
        console.log('Genres fetched:', genres);
        this.genres = genres;
      });
  }

  // Handle deletion of a genre
  deleteGenre(genre: Genre): void {
    if (confirm(`Are you sure you want to delete ${genre.name}?`)) {
      this.genres = this.genres.filter((g) => g !== genre);
      this.genreService.deleteGenre(genre.id).subscribe(() => {
        console.log('Genre deleted:', genre);
      });
    }
  }

  // Handle adding a new genre
  addGenre(): void {
    if (this.newGenre) {
      if (
        this.genres.some(
          (genre) => genre.name.toLowerCase() === this.newGenre.toLowerCase()
        )
      ) {
        this.errorMessage = 'Genre already exists.'; // Set error message if genre exists
      } else {
        this.genreService.addGenre(this.newGenre).subscribe(
          (genre) => {
            this.genres.push(genre);
            this.newGenre = '';
            this.errorMessage = ''; // Clear the error message on successful addition
          },
          (error) => {
            console.error('Error adding genre:', error);
            this.errorMessage = 'Failed to add genre.'; // Set error message on failure
          }
        );
      }
    }
  }

  // Navigate back
  goBack(): void {
    this.location.back();
  }
}
