import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private baseUrl = 'http://localhost:8080/movies';

  constructor(private http: HttpClient) { }

  // Get movies with optional pagination parameters
  getMovies(page: number = 0, size: number = 5): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(this.baseUrl, { params }).pipe(
      tap(data => console.log('Data from getMovies:', data))
    );
  }

  // Get a movie by ID
  getMovie(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  // Add a new movie
  addMovie(movie: any): Observable<Object> {
    return this.http.post(this.baseUrl, movie);
  }

  // Update a movie by ID
  updateMovie(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }

  // Delete a movie by ID
  deleteMovie(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  // Search movies by searchTerm with optional pagination parameters
  searchMovies(searchTerm: string, page: number, size: number): Observable<any> {
    const params = new HttpParams()
      .set('query', searchTerm)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.baseUrl}/search`, { params }).pipe(
      tap(data => {
        console.log('Data from searchMovies:', data);
        const movies = data.content.map((movie: any) => ({
          ...movie,
          genreNames: movie.genres.map((g: any) => g.name).join(', '),
          actorNames: movie.actors.map((a: any) => a.name).join(', ')
        }));
        return { ...data, content: movies };
      })
    );
  }
}