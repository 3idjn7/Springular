import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from './components/product-components/product-list/product-list.component';
import { ProductDetailsComponent } from './components/product-components/product-details/product-details.component';
import { ProductCreateComponent } from './components/product-components/product-create/product-create.component';
import { ProductEditComponent } from './components/product-components/product-edit/product-edit.component';
import { MovieFormComponent } from './components/movies-components/movie-form/movie-form.component';
import { MoviesComponent } from './components/movies-components/movies/movies.component';
import { HomeComponent } from './components/home/home.component';
import { ActorsComponent } from './components/movies-components/actors/actors.component';
import { GenresComponent } from './components/movies-components/genres/genres.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'products', component: ProductListComponent },
  { path: 'product/details/:id', component: ProductDetailsComponent },
  { path: 'product/add', component: ProductCreateComponent },
  { path: 'product/edit/:id', component: ProductEditComponent },
  { path: 'movies', component: MoviesComponent },
  { path: 'movie/edit/:id', component: MovieFormComponent },
  { path: 'add-movie', component: MovieFormComponent },
  { path: 'add-actor', component: ActorsComponent },
  { path: 'add-genre', component: GenresComponent },
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
