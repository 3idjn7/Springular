package com.example.crudspringular.service;

import com.example.crudspringular.entity.Product;
import com.example.crudspringular.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

    public Optional<Product> findProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        log.info("Saving product: {}", product);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }
}
