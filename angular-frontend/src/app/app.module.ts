import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { ProductListComponent } from './components/product-components/product-list/product-list.component';
import { ProductDetailsComponent } from './components/product-components/product-details/product-details.component';
import { ProductCreateComponent } from './components/product-components/product-create/product-create.component';
import { ProductEditComponent } from './components/product-components/product-edit/product-edit.component';
import { MoviesComponent } from './components/movies-components/movies/movies.component';
import { ActorsComponent } from './components/movies-components/actors/actors.component';
import { GenresComponent } from './components/movies-components/genres/genres.component';
import { MovieFormComponent } from './components/movies-components/movie-form/movie-form.component';
import { HomeComponent } from './components/home/home.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CeilPipe } from './ceil.pipe';



@NgModule({
  declarations: [
    AppComponent,
    ProductListComponent,
    ProductDetailsComponent,
    ProductCreateComponent,
    ProductEditComponent,
    MoviesComponent,
    ActorsComponent,
    GenresComponent,
    MovieFormComponent,
    HomeComponent,
    CeilPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
