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

  constructor(private genreService: GenreService) { }

  ngOnInit() {
    console.log('GenresComponent initialized');
    this.getGenres();
  }
  
  // This retrieves the genres when the component initializes.
  getGenres(): void {
    console.log('Fetching genres...');
    this.genreService.getGenres()
      .subscribe(genres => {
        console.log('Genres fetched:', genres);
        this.genres = genres;
      });
  }

  // Method to handle deletion of a genre
  delete(genre: Genre): void {
    console.log('Deleting genre:', genre);
    this.genres = this.genres.filter(g => g !== genre);
    this.genreService.deleteGenre(genre.id).subscribe(() => {
      console.log(`Genre with id ${genre.id} deleted`);
    });
  }

  // Method to handle adding a new genre
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
}
