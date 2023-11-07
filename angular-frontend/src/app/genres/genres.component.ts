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
  newGenre: string = ''; // New property for binding with the input for adding a new genre

  constructor(private genreService: GenreService) {}

  ngOnInit() {
    this.getGenres();
  }
  
  // This retrieves the genres when the component initializes.
  getGenres(): void {
    this.genreService.getGenres()
      .subscribe(genres => this.genres = genres);
  }

  // Method to handle deletion of a genre
  delete(genre: Genre): void {
    this.genres = this.genres.filter(g => g !== genre);
    this.genreService.deleteGenre(genre.id).subscribe();
  }

  // Method to handle adding a new genre
  addGenre(): void {
    if (this.newGenre) { // Only attempt to add if the field is not empty
      this.genreService.addGenre(this.newGenre).subscribe(genre => {
        this.genres.push(genre); // Add the new genre to the local array
        this.newGenre = ''; // Reset the input after adding
      });
    }
  }
}
