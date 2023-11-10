// movie.model.ts
import { Genre } from './genre.model';
import { Actor } from './actor.model';

export interface Movie {
  id: number;
  title: string;
  releaseYear: number;
  genres: Genre[];
  actors: Actor[];
}