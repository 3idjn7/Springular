package com.example.crudspringular.controller;

import com.example.crudspringular.entity.Product;
import com.example.crudspringular.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        log.info("Requested to update product with id: {}", id);

        return productService.findProductById(id)
                .map(existingProduct -> {
                    log.info("Product found with id: {}. Updating product...", id);
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPrice(productDetails.getPrice());

                    Product updatedProduct = productService.saveProduct(existingProduct);
                    log.info("Product with id: {} updated successfully.", id);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElseGet(() -> {
                    log.warn("Product with id: {} not found. Cannot update.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Requested to delete product with id: {}", id);

        Optional<Product> product = productService.findProductById(id);
        if (product.isPresent()) {
            log.info("Product found with id: {}. Deleting product...", id);
            productService.deleteProduct(id);
            log.info("Product with id: {} deleted successfully.", id);
            return ResponseEntity.ok().build(); // This returns ResponseEntity<Void>
        } else {
            log.warn("Product with id: {} not found. Cannot delete.", id);
            return ResponseEntity.notFound().build();
        }
    }


}
