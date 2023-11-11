import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClientModule } from '@angular/common/http';
import { ActorService } from './actor.service';

describe('ActorService', () => {
  let service: ActorService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, HttpClientModule], // Import HttpClientTestingModule
      providers: [ActorService], // Add your service to the providers array
    });

    // Create an instance of HttpTestingController
    httpTestingController = TestBed.inject(HttpTestingController); // Add this line
    service = TestBed.inject(ActorService);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch actors', () => {
    const mockActors = [{ id: 1, name: 'Actor 1' }, { id: 2, name: 'Actor 2' }];
    
    service.getActors().subscribe((actors) => {
      expect(actors).toEqual(mockActors);
    });

    const req = httpTestingController.expectOne('http://localhost:8080/actors');
    expect(req.request.method).toEqual('GET');
    req.flush(mockActors);
  });

  // Add more tests for addActor and deleteActor methods
});
