import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { MovieFormComponent } from './movie-form.component';
import { MovieService } from '../../services/movie.service';
import { ActorService } from '../../services/actor.service';
import { GenreService } from '../../services/genre.service';
import { of } from 'rxjs';

describe('MovieFormComponent', () => {
  let component: MovieFormComponent;
  let fixture: ComponentFixture<MovieFormComponent>;
  let mockMovieService: jasmine.SpyObj<MovieService>;
  let mockActorService: jasmine.SpyObj<ActorService>;
  let mockGenreService: jasmine.SpyObj<GenreService>;
  let mockRouter: jasmine.SpyObj<Router>;
  let mockLocation: jasmine.SpyObj<Location>;
  let mockActivatedRoute: any;

  beforeEach(() => {
    mockMovieService = jasmine.createSpyObj('MovieService', ['addMovie', 'updateMovie', 'getMovie']);
    mockActorService = jasmine.createSpyObj('ActorService', ['getActors']);
    mockGenreService = jasmine.createSpyObj('GenreService', ['getGenres']);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);
    mockLocation = jasmine.createSpyObj('Location', ['back']);
    mockActivatedRoute = {
      snapshot: {
        params: {},
      },
      queryParams: of({ page: '0' }),
      pipe: (callback: any) => {
        // Simulate the behavior of the pipe method by calling the provided callback
        return callback(of({ page: '0' }));
      },
    };

    TestBed.configureTestingModule({
      declarations: [MovieFormComponent],
      imports: [ReactiveFormsModule],
      providers: [
        FormBuilder,
        { provide: MovieService, useValue: mockMovieService },
        { provide: ActorService, useValue: mockActorService },
        { provide: GenreService, useValue: mockGenreService },
        { provide: Router, useValue: mockRouter },
        { provide: Location, useValue: mockLocation },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    });

    fixture = TestBed.createComponent(MovieFormComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch actors and genres on ngOnInit', () => {
    const mockActors = [{ id: 1, name: 'Actor 1' }];
    const mockGenres = [{ id: 1, name: 'Genre 1' }];

    mockActorService.getActors.and.returnValue(of(mockActors));
    mockGenreService.getGenres.and.returnValue(of(mockGenres));

    fixture.detectChanges();

    expect(component.actors).toEqual(mockActors);
    expect(component.genres).toEqual(mockGenres);
  });

  it('should submit the form when valid', () => {
    const mockMovieData = {
      title: 'Movie 1',
      director: 'Director 1',
      releaseYear: 2023,
      actors: [1],
      genres: [1],
    };

    component.movieForm.setValue(mockMovieData);
    mockMovieService.addMovie.and.returnValue(of({}));

    component.onSubmit();

    expect(mockMovieService.addMovie).toHaveBeenCalledWith(mockMovieData);
  });

  it('should update the movie when in edit mode', () => {
    const mockMovieId = '1';
      mockActivatedRoute.snapshot.params = { 'id': mockMovieId };
    const mockMovieData = {
      title: 'Updated Movie',
      director: 'Updated Director',
      releaseYear: 2023,
      actors: [1],
      genres: [1],
    };

    component.movieForm.setValue(mockMovieData);
    component.isEdit = true;
    mockMovieService.updateMovie.and.returnValue(of({}));

    component.onSubmit();

    const movieId = mockActivatedRoute.snapshot.params['id'];
    expect(mockMovieService.updateMovie).toHaveBeenCalledWith(movieId, mockMovieData);
  });

  it('should go back when goBack is called', () => {
    component.currentPage = 2;
    component.goBack();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/movies'], { queryParams: { page: 2 } });
  });
  // Add more tests to cover additional component functionality
});
