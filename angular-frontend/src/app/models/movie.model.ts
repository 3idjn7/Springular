// movie.model.ts
import { Genre } from './genre.model';
import { Actor } from './actor.model';

export class Movie {
  id!: number;
  title: string;
  releaseYear?: number;
  director?: string;
  genres?: Genre[];
  actors?: Actor[];

  constructor(
    title: string = '',
    releaseYear: number = new Date().getFullYear(),
    director?: string,
    genres: Genre[] = [],
    actors: Actor[] = [],
  ) {
    this.title = title;
    this.releaseYear = releaseYear;
    this.director = director;
    this.genres = genres;
    this.actors = actors;
  }
}