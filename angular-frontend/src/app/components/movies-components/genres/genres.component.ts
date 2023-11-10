import { Component, OnInit, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { GenreService } from '../../services/genre.service';
import { Genre } from '../../models/genre.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-genres',
  templateUrl: './genres.component.html',
  styleUrls: ['./genres.component.css']
})
export class GenresComponent implements OnInit, OnDestroy {
  genres: Genre[] = [];
  newGenre: string = '';
  private unsubscribe$ = new Subject<void>();

  constructor(
    private genreService: GenreService,
    private location: Location,
  ) { }

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
    this.genreService.getGenres()
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(genres => {
        console.log('Genres fetched:', genres);
        this.genres = genres;
      });
  }

  // Handle deletion of a genre
  deleteGenre(genre: Genre): void {
    if (confirm(`Are you sure you want to delete ${genre.name}?`)) {
      this.genres = this.genres.filter(g => g !== genre);
      this.genreService.deleteGenre(genre.id).subscribe(() => {
        console.log('Genre deleted:', genre);
      });
    }
  }

  // Handle adding a new genre
  addGenre(): void {
    if (this.newGenre) {
      console.log('Adding genre:', this.newGenre);
      this.genreService.addGenre(this.newGenre).subscribe(genre => {
        console.log('Genre added:', genre);
        this.genres.push(genre);
        this.newGenre = '';
      });
    } else {
      console.log('New genre input is empty. No genre added.');
    }
  }

  // Navigate back
  goBack(): void {
    this.location.back();
  }
}
