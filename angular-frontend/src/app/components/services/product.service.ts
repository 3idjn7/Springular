import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, of } from 'rxjs';
import { Product } from '../models/product.model';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsUrl = 'http://localhost:8080/api/products'; // URL to web API

  constructor(private http: HttpClient) { }

  // Get all products
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.productsUrl);
  }

  // Get product by id
  getProduct(id: number): Observable<Product> {
    const url = `${this.productsUrl}/${id}`;
    return this.http.get<Product>(url);
  }

  // Add a new product
  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.productsUrl, product, httpOptions);
  }

  // Update a product
  updateProduct(id: number, product: Product): Observable<Product> {
    const url = `${this.productsUrl}/${id}`;
    return this.http.put<Product>(url, product, httpOptions).pipe(
      catchError(this.handleError<Product>('updateProduct'))
    );
  }

  // Delete a product
  deleteProduct(id: number): Observable<any> {
    const url = `${this.productsUrl}/${id}`;
    return this.http.delete(url, httpOptions).pipe(
      catchError(this.handleError<any>('deleteProduct'))
    );
  }

  // Handle HTTP operation that failed and let the app continue.
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}