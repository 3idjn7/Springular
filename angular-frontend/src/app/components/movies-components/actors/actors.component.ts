import { Component, OnInit, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { ActorService } from '../../services/actor.service';
import { Actor } from '../../models/actor.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-actors',
  templateUrl: './actors.component.html',
  styleUrls: ['./actors.component.css']
})
export class ActorsComponent implements OnInit, OnDestroy {
  actors: Actor[] = [];
  newActor: string = '';
  errorMessage: string = '';
  private unsubscribe$ = new Subject<void>();

  constructor(
    private actorService: ActorService,
    private location: Location,
  ) { }

  ngOnInit() {
    // Initialize component
    this.getActors();
  }

  ngOnDestroy() {
    // Unsubscribe from observables to prevent memory leaks
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  // Retrieve actors when the component initializes
  getActors(): void {
    console.log('Fetching actors...');
    this.actorService.getActors()
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(actors => {
        console.log('Actors fetched:', actors);
        this.actors = actors;
      });
  }

  // Handle deletion of an actor
  deleteActor(actor: Actor): void {
    if (confirm(`Are you sure you want to delete ${actor.name}?`)) {
      this.actors = this.actors.filter(a => a !== actor);
      this.actorService.deleteActor(actor.id).subscribe(() => {
        console.log('Actor deleted:', actor);
      });
    }
  }

  // Handle adding a new actor
  addActor(): void {
    if (this.newActor) {
      if (this.actors.some(actor => actor.name.toLowerCase() === this.newActor.toLowerCase())) {
        this.errorMessage = 'Actor already exists.';
      } else {
        this.actorService.addActor(this.newActor).subscribe(
          actor => {
            this.actors.push(actor);
            this.newActor = '';
            this.errorMessage = '';
          },
          error => {
            console.error('Error adding actor:', error);
            this.errorMessage = 'Failed to add actor.';
          }
        );
      }
    }
  }

  // Navigate back
  goBack(): void {
    this.location.back();
  }
}
