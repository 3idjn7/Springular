import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private baseUrl = 'http://localhost:8080/movies';

  constructor(private http: HttpClient) { }

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
}
