import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, Subject } from 'rxjs';
import { MoviesComponent } from './movies.component';
import { MovieService } from '../../services/movie.service';
import { FormsModule } from '@angular/forms';
import { takeUntil } from 'rxjs/operators';

describe('MoviesComponent', () => {
  let component: MoviesComponent;
  let fixture: ComponentFixture<MoviesComponent>;
  let mockMovieService: jasmine.SpyObj<MovieService>;
  let mockRouter: jasmine.SpyObj<Router>;
  let mockActivatedRoute: jasmine.SpyObj<ActivatedRoute>;

  beforeEach(() => {
    mockMovieService = jasmine.createSpyObj('MovieService', [
      'getMovies',
      'searchMovies',
      'deleteMovie',
    ]);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);
    mockActivatedRoute = jasmine.createSpyObj('ActivatedRoute', [], {
      queryParams: of({ page: 1, searchTerm: '' }), // Empty searchTerm
    });

    TestBed.configureTestingModule({
      declarations: [MoviesComponent],
      imports: [FormsModule],
      providers: [
        { provide: MovieService, useValue: mockMovieService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    });

    fixture = TestBed.createComponent(MoviesComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize currentPage and searchTerm from queryParams on ngOnInit', fakeAsync(() => {
    spyOn(component, 'reloadData');

    // Trigger ngOnInit explicitly
    fixture.detectChanges();
    tick(); // Wait for async operations to complete

    expect(component.currentPage).toBe(1); // Assuming queryParams contain 'page: 1'
    expect(component.searchTerm).toBe(''); // Empty searchTerm
  }));

  it('should call reloadData when searchTerm is empty on ngOnInit', fakeAsync(() => {
    spyOn(component, 'reloadData');
    mockActivatedRoute.queryParams = of({ page: 1, searchTerm: '' });

    // Trigger ngOnInit explicitly
    fixture.detectChanges();
    tick(); // Wait for async operations to complete

    // Expect the reloadData method to have been called with the correct arguments
    expect(component.reloadData).toHaveBeenCalledWith(1);
  }));

  // Other tests...

});
