import { Component, OnInit } from '@angular/core';
import { GenreService } from '../services/genre.service';
import { Genre } from '../models/genre.model';

@Component({
  selector: 'app-genres',
  templateUrl: './genres.component.html',
  styleUrls: ['./genres.component.css']
})
export class GenresComponent implements OnInit {
  genres: Genre[] = [];

  constructor(private genreService: GenreService) {}

  ngOnInit() {
    this.getGenres();
  }

  getGenres(): void {
    this.genreService.getGenres()
      .subscribe(genres => this.genres = genres);
  }

  delete(genre: Genre): void {
    this.genres = this.genres.filter(g => g !== genre);
    this.genreService.deleteGenre(genre.id).subscribe();
  }

  // Add more methods for editing, adding genres if needed
}
