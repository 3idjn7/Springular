// actor.service.ts
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

  getActors(): Observable<Actor[]> {
    return this.http.get<Actor[]>(this.actorsUrl);
  }

  getActor(id: number): Observable<Actor> {
    const url = `${this.actorsUrl}/${id}`;
    return this.http.get<Actor>(url);
  }

  addActor(name: string): Observable<Actor> {
    return this.http.post<Actor>(this.actorsUrl, { name });
  }

  deleteActor(id: number): Observable<any> {
    const url = `${this.actorsUrl}/${id}`;
    return this.http.delete(url);
  }

  updateActor(actor: Actor): Observable<any> {
    return this.http.put(this.actorsUrl, actor);
  }

  findOrCreateActor(name: string): Observable<Actor> {
    return this.http.post<Actor>(`${this.actorsUrl}/findOrCreate`, { name });
  }
}
