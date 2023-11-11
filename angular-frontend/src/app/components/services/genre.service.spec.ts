import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClientModule } from '@angular/common/http';
import { GenreService } from './genre.service';

describe('GenreService', () => {
  let service: GenreService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, HttpClientModule], // Import HttpClientTestingModule
      providers: [GenreService], // Add your service to the providers array
    });

    // Create an instance of HttpTestingController
    httpTestingController = TestBed.inject(HttpTestingController); // Add this line
    service = TestBed.inject(GenreService);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch genres', () => {
    const mockGenres = [{ id: 1, name: 'Action' }, { id: 2, name: 'Thriller' }];
    
    service.getGenres().subscribe((genres) => {
      expect(genres).toEqual(mockGenres);
    });

    const req = httpTestingController.expectOne('http://localhost:8080/genres');
    expect(req.request.method).toEqual('GET');
    req.flush(mockGenres);
  });

  // Add more tests for addGenre and deleteGenre methods
});
