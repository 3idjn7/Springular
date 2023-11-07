import { Component, OnInit } from '@angular/core';
import { ActorService } from '../services/actor.service';
import { Actor } from '../models/actor.model';

@Component({
  selector: 'app-actors',
  templateUrl: './actors.component.html',
  styleUrls: ['./actors.component.css']
})
export class ActorsComponent implements OnInit {
  actors: Actor[] = [];
  newActor: string = '';

  constructor(private actorService: ActorService) {}

  ngOnInit() {
    this.getActors();
  }

  getActors(): void {
    this.actorService.getActors()
      .subscribe(actors => this.actors = actors);
  }

  delete(actor: Actor): void {
    this.actors = this.actors.filter(h => h !== actor);
    this.actorService.deleteActor(actor.id).subscribe();
  }

  addActor(): void {
    if (this.newActor) {
      this.actorService.addActor(this.newActor).subscribe(actor => {
        this.actors.push(actor);
        this.newActor = ''; // Reset the input after adding
      });
    }
  }

  // Add more methods for editing, adding actors if needed
}
