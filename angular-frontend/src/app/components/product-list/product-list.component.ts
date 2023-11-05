import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];

  constructor(
    private productService: ProductService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts(): void {
  this.productService.getProducts().subscribe({
    next: (data: Product[]) => {
      this.products = data;
    },
    error: error => {
      console.error('Error fetching products!', error);
    },
    complete: () => {
      console.log('Product fetching completed');
    }
  });
}

  createProduct(): void {
    console.log('Navigating to product creation page');
    this.router.navigate(['/create']);
  }

  editProduct(id: number): void {
    console.log('Navigating to edit page for product with id:', id);
    this.router.navigate(['/edit', id]);
  }

  deleteProduct(id: number): void {
     console.log('Attempting to delete product with id:', id); // Log the id of the product being deleted
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
           console.log('Product deleted successfully!');
           this.loadProducts();
        },
        error: (error) => {
           console.error('Error deleting product', error); // Log the error
        }
      });
    }
  }

}
