import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Actor } from '../models/actor.model';

@Injectable({
  providedIn: 'root'
})
export class ActorService {
  private actorsUrl = 'http://localhost:8080/actors';

  constructor(private http: HttpClient) {}

  // Get all actors
  getActors(): Observable<Actor[]> {
    return this.http.get<Actor[]>(this.actorsUrl);
  }

  // Add a new actor with a name
  addActor(name: string): Observable<Actor> {
    return this.http.post<Actor>(this.actorsUrl, { name });
  }

  // Delete an actor by ID
  deleteActor(id: number): Observable<any> {
    const url = `${this.actorsUrl}/${id}`;
    return this.http.delete(url);
  }
}
