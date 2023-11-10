import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActorService } from '../../services/actor.service';
import { Actor } from '../../models/actor.model';

@Component({
  selector: 'app-actors',
  templateUrl: './actors.component.html',
  styleUrls: ['./actors.component.css']
})
export class ActorsComponent implements OnInit {
  actors: Actor[] = [];
  newActor: string = '';

  constructor(
    private actorService: ActorService,
    private location: Location,
  ) { }

  ngOnInit() {
    // Initialize component
    this.getActors();
  }
  
  // Retrieve actors when the component initializes
  getActors(): void {
    console.log('Fetching actors...');
    this.actorService.getActors()
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
      this.actorService.addActor(this.newActor).subscribe(actor => {
        this.actors.push(actor);
        this.newActor = '';
      });
    }
  }

  // Navigate back
  goBack(): void {
    this.location.back();
  }
}
