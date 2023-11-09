import { Component, OnInit } from '@angular/core';
import { MovieService } from '../services/movie.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';



@Component({
  selector: 'app-movies',
  templateUrl: './movies.component.html',
  styleUrls: ['./movies.component.css']
})
export class MoviesComponent implements OnInit {
  private unsubscribe$ = new Subject<void>();

  movies!: any[];
  searchTerm: string = '';
  currentPage: number = 0;
  pageSize: number = 5;
  totalItems: number = 0;

  constructor(
    private movieService: MovieService,
    private router: Router,
    private route: ActivatedRoute,
  ) { }

  ngOnInit() {
      this.route.queryParams.pipe(takeUntil(this.unsubscribe$)).subscribe(params => {
        this.currentPage = params['page'] ? parseInt(params['page'], 10) : 0;
        this.searchMovies(this.currentPage);
      });
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  reloadData() {
    console.log('Reloading movie data...');
    this.movieService.getMovies().subscribe(data => {
        // Log raw movie data
      console.log('Data from getMovies:', data);

          const processedMovies = data.map((movie: any) => {
          // Log the raw genre and actors to see their structure for each movie
          console.log('Processing movie:', movie);
          console.log('Raw genre:', movie.genre);
          console.log('Raw actors:', movie.actors);
          const genreNames = movie.genres && movie.genres.length > 0
              ? movie.genres.map((g: { name: any; }) => g.name).join(', ')
              : 'N/A';

          const processedMovie = {
            ...movie,
            genreNames: genreNames,
            actorNames: movie.actors && movie.actors.length > 0
                        ? movie.actors.map((actor: any) => actor.name).join(', ')
                        : 'No actors',
            releaseYear: movie.releaseYear || 'Unknown',
          };
          console.log('Processed movie:', processedMovie);

          return processedMovie;
        });
        console.log('Movies with genres processed:', processedMovies);

        this.movies = processedMovies;
      }, error => console.log('Error fetching movies:', error));
  }



  addMovie(): void {
    this.router.navigate(['/add-movie']);
  }

  addActor(): void {
    this.router.navigate(['/add-actor']);
  }

  addGenre(): void {
    this.router.navigate(['/add-genre']); 
  }

  deleteMovie(id: number) {
    this.movieService.deleteMovie(id)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  updateMovie(id: number) {
    this.router.navigate(['/movie/edit', id]);
  }

  searchMovies(page: number = this.currentPage): void {
  this.movieService.searchMovies(this.searchTerm, page, this.pageSize).subscribe(data => {
    const processedMovies = data['content'].map((movie: any) => {
      const genreNames = movie.genres && movie.genres.length > 0
        ? movie.genres.map((g: { name: any; }) => g.name).join(', ')
        : 'N/A';
      const actorNames = movie.actors && movie.actors.length > 0
        ? movie.actors.map((actor: any) => actor.name).join(', ')
        : 'No actors';
      return {
        ...movie,
        genreNames: genreNames,
        actorNames: actorNames,
      };
    });

    this.movies = processedMovies;
    this.totalItems = data['totalElements']; // This should be the total number of items for pagination
    this.currentPage = page;
    }, error => console.log('Error searching for movies:', error));
  }

    getPages(totalItems: number, pageSize: number): number[] {
  const totalPages = Math.ceil(totalItems / pageSize);
  return Array.from({ length: totalPages }, (_, index) => index);
}


  onPageChange(page: number): void {
    this.currentPage = page;
    this.searchMovies(page);
  }
}
