import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit {
  product: Product = {
    id: 0,
    name: '',
    price: 0,
    description: '',
  };
  id!: number;

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit() {
    this.route.params.subscribe({
      next: (params) => {
        this.id = params['id'];
        if (this.id) {
          this.productService.getProduct(this.id).subscribe({
            next: (data: Product) => {
              this.product = data;
            },
            error: (error) => {
              console.error('Error fetching product!', error);
            }
          });
        }
      },
      error: (error) => {
        console.error('Error with route params!', error);
      }
    });
  }

  saveProduct() {
  console.log('Attempting to save product', this.product); // Log the product being saved
  if (this.id) {
    this.productService.updateProduct(this.id, this.product).subscribe({
      next: (data) => {
        console.log('Product updated successfully!', data); // Log the response data
        this.router.navigate(['/products']);
      },
      error: (error) => {
        console.error('Error updating product!', error); // Log the error
      }
    });
  } else {
    console.error('Product ID is not set!');
    // Handle the case when product ID is not set appropriately
  }
}

}
