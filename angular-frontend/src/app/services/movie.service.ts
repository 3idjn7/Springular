// movie.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private baseUrl = 'http://localhost:8080/movies';

  constructor(
    private http: HttpClient,
  ) { }

  getMovies(): Observable<any> {
  return this.http.get(`${this.baseUrl}`).pipe(
    tap(data => console.log('Data from getMovies:', data))
  );
}

  getMovie(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  addMovie(movie: any): Observable<Object> {
    return this.http.post(`${this.baseUrl}`, movie);
  }

  updateMovie(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }

  deleteMovie(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  searchMovies(searchTerm: string, page: number, size: number): Observable<any> {
  const params = new HttpParams()
    .set('query', searchTerm) // This parameter name must match with your backend API
    .set('page', page.toString())
    .set('size', size.toString());
  return this.http.get<any>(`${this.baseUrl}/search`, { params })
    .pipe(
      tap(data => {
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
