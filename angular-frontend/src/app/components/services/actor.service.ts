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

  // Get an actor by ID
  getActor(id: number): Observable<Actor> {
    const url = `${this.actorsUrl}/${id}`;
    return this.http.get<Actor>(url);
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

  // Update an actor
  updateActor(actor: Actor): Observable<any> {
    return this.http.put(this.actorsUrl, actor);
  }

  // Find or create an actor by name
  findOrCreateActor(name: string): Observable<Actor> {
    return this.http.post<Actor>(`${this.actorsUrl}/findOrCreate`, { name });
  }
}
