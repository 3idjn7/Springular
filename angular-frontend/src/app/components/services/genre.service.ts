import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Genre } from '../models/genre.model';

@Injectable({
  providedIn: 'root'
})
export class GenreService {
  private genresUrl = 'http://localhost:8080/genres';

  constructor(private http: HttpClient) {}

  // Get all genres
  getGenres(): Observable<Genre[]> {
    console.log('GenreService: getGenres called');
    return this.http.get<Genre[]>(this.genresUrl);
  }

  // Add a new genre with a name
  addGenre(name: string): Observable<Genre> {
    console.log(`GenreService: addGenre called with name '${name}'`);
    return this.http.post<Genre>(this.genresUrl, { name });
  }

  // Delete a genre by ID
  deleteGenre(id: number): Observable<any> {
    console.log(`GenreService: deleteGenre called with id ${id}`);
    return this.http.delete(`${this.genresUrl}/${id}`);
  }
}