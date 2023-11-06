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

  getGenres(): Observable<Genre[]> {
    return this.http.get<Genre[]>(this.genresUrl);
  }

  getGenre(id: number): Observable<Genre> {
    const url = `${this.genresUrl}/${id}`;
    return this.http.get<Genre>(url);
  }

  addGenre(genre: Genre): Observable<Genre> {
    return this.http.post<Genre>(this.genresUrl, genre);
  }

  deleteGenre(id: number): Observable<any> {
    const url = `${this.genresUrl}/${id}`;
    return this.http.delete(url);
  }

  updateGenre(genre: Genre): Observable<any> {
    return this.http.put(this.genresUrl, genre);
  }

  findOrCreateGenre(name: string): Observable<Genre> {
    return this.http.post<Genre>(`${this.genresUrl}/findOrCreate`, { name });
  }
}
