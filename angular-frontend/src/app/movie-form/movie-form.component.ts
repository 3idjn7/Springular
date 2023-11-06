import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { MovieService } from '../services/movie.service';
import { GenreService } from '../services/genre.service';
import { ActorService } from '../services/actor.service';

@Component({
  selector: 'app-movie-form',
  templateUrl: './movie-form.component.html',
  styleUrls: ['./movie-form.component.css']
})
export class MovieFormComponent implements OnInit {
  movie: any = {
    title: '',
    releaseYear: null,
    genres: [],
    actors: []
  }; 
  genreInput: string = '';
  actorsInput: string = '';
  editMode: boolean = false;

  constructor(
    private movieService: MovieService,
    private genreService: GenreService,
    private actorService: ActorService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const movieId = params.get('id');
      if (movieId) {
        this.editMode = true;
        this.movieService.getMovie(+movieId).subscribe(movie => {
          this.movie = movie;
          this.genreInput = movie.genres.map((genre: { name: any; }) => genre.name).join(', ');
          this.actorsInput = movie.actors.map((actor: { name: any; }) => actor.name).join(', ');
        });
      }
    });
  }

  saveMovie(): void {
    const genreNames = this.genreInput.split(',').map(name => name.trim());
    const genreObservables = genreNames.map(name => this.genreService.findOrCreateGenre(name));
    
    forkJoin(genreObservables).subscribe((genres: any[]) => { 
      const genreIds = genres.map(genre => genre.id);
      
      const actorNames = this.actorsInput.split(',').map(name => name.trim());
      const actorObservables = actorNames.map(name => this.actorService.findOrCreateActor(name));
      
      forkJoin(actorObservables).subscribe((actors: any[]) => {
        const actorIds = actors.map(actor => actor.id);
        
        const movieDto = {
          title: this.movie.title,
          releaseYear: this.movie.releaseYear,
          genreIds: genreIds,
          actorIds: actorIds
        };

        if (this.editMode) {
          this.movieService.updateMovie(this.movie.id, movieDto).subscribe(() => {
            this.router.navigate(['/movies']);
          });
        } else {
          this.movieService.addMovie(movieDto).subscribe(() => {
            this.router.navigate(['/movies']);
          });
        }
      });
    });
  }

  cancel(): void {
    this.router.navigate(['/movies']);
  }
}
