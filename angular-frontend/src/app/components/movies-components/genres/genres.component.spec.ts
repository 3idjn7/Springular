import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { GenresComponent } from './genres.component';
import { GenreService } from '../../services/genre.service';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';

describe('GenresComponent', () => {
  let component: GenresComponent;
  let fixture: ComponentFixture<GenresComponent>;
  let genreService: jasmine.SpyObj<GenreService>;

  beforeEach(() => {
    // Create a spy object for GenreService
    genreService = jasmine.createSpyObj('GenreService', ['getGenres', 'addGenre', 'deleteGenre']);

    TestBed.configureTestingModule({
      imports: [FormsModule, HttpClientModule], // Import FormsModule and HttpClientModule
      declarations: [GenresComponent], // Declare your component
      providers: [
        // Provide the spy object as a value for the GenreService token
        { provide: GenreService, useValue: genreService }
      ],
    });

    fixture = TestBed.createComponent(GenresComponent);
    component = fixture.componentInstance;

    // Configure the spy behavior for getGenres
    genreService.getGenres.and.returnValue(of([{ id: 1, name: 'Genre 1' }]));

    fixture.detectChanges();
  });

  it('should fetch genres', () => {
    expect(component.genres.length).toBe(1);
    expect(component.genres[0].name).toBe('Genre 1');
  });

  it('should add a new genre', () => {
    const newGenreName = 'New Genre';
    const newGenre = { id: 2, name: newGenreName };

    // Configure the spy behavior for addGenre
    genreService.addGenre.and.returnValue(of(newGenre));

    component.newGenre = newGenreName;
    component.addGenre();

    expect(genreService.addGenre).toHaveBeenCalledWith(newGenreName);
    expect(component.genres).toContain(newGenre);
    expect(component.newGenre).toBe('');
  });

  it('should delete a genre', () => {
    const genreToDelete = { id: 1, name: 'Genre 1' };

    // Configure the spy behavior for deleteGenre
    genreService.deleteGenre.and.returnValue(of(null));

    component.genres = [genreToDelete, { id: 2, name: 'Genre 2' }];
    component.deleteGenre(genreToDelete);

    expect(genreService.deleteGenre).toHaveBeenCalledWith(genreToDelete.id);
    expect(component.genres).not.toContain(genreToDelete);
  });

  // Add more tests to cover other component methods and behavior
});
