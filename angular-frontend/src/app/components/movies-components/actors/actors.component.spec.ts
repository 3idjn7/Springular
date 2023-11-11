import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { ActorsComponent } from './actors.component';
import { ActorService } from '../../services/actor.service';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';

describe('ActorsComponent', () => {
  let component: ActorsComponent;
  let fixture: ComponentFixture<ActorsComponent>;
  let actorService: jasmine.SpyObj<ActorService>;

  beforeEach(() => {
    // Create a spy object for ActorService
    actorService = jasmine.createSpyObj('ActorService', ['getActors', 'addActor', 'deleteActor']);

    TestBed.configureTestingModule({
      imports: [FormsModule, HttpClientModule], // Import FormsModule and HttpClientModule
      declarations: [ActorsComponent], // Declare your component
      providers: [
        // Provide the spy object as a value for the ActorService token
        { provide: ActorService, useValue: actorService }
      ],
    });

    fixture = TestBed.createComponent(ActorsComponent);
    component = fixture.componentInstance;

    // Configure the spy behavior for getActors
    actorService.getActors.and.returnValue(of([{ id: 1, name: 'Actor 1' }]));

    fixture.detectChanges();
  });

  it('should fetch actors', () => {
    expect(component.actors.length).toBe(1);
    expect(component.actors[0].name).toBe('Actor 1');
  });

  it('should add a new actor', () => {
    const newActorName = 'New Actor';
    const newActor = { id: 2, name: newActorName };

    // Configure the spy behavior for addActor
    actorService.addActor.and.returnValue(of(newActor));

    component.newActor = newActorName;
    component.addActor();

    expect(actorService.addActor).toHaveBeenCalledWith(newActorName);
    expect(component.actors).toContain(newActor);
    expect(component.newActor).toBe('');
  });

  it('should delete an actor', () => {
    const actorToDelete = { id: 1, name: 'Actor 1' };

    // Configure the spy behavior for deleteActor
    actorService.deleteActor.and.returnValue(of(null));

    component.actors = [actorToDelete, { id: 2, name: 'Actor 2' }];
    component.deleteActor(actorToDelete);

    expect(actorService.deleteActor).toHaveBeenCalledWith(actorToDelete.id);
    expect(component.actors).not.toContain(actorToDelete);
  });

  // Add more tests to cover other component methods and behavior
});
