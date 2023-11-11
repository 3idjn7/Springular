import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { MovieService } from './movie.service';
import { HttpClientModule } from '@angular/common/http';

describe('MovieService', () => {
  let service: MovieService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, HttpClientModule],
      providers: [MovieService],
    });

    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(MovieService);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch movies', () => {
    const mockMovies = [{ id: 1, title: 'Movie 1' }, { id: 2, title: 'Movie 2' }];

    service.getMovies().subscribe((movies) => {
      expect(movies).toEqual(mockMovies);
    });

    const req = httpTestingController.expectOne('http://localhost:8080/movies?page=0&size=5'); // Specify the full URL with query parameters
    expect(req.request.method).toEqual('GET');
    req.flush(mockMovies);
  });

  // Add more tests for other MovieService methods
});
