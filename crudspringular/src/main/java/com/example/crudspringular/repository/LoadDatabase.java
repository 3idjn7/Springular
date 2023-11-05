/*package com.example.crudspringular.repository;

import com.example.crudspringular.entity.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Product(null, "Sample Product 1", "Description for product 1", 10.0));
                repository.save(new Product(null, "Sample Product 2", "Description for product 2", 20.0));
                // Add more products as needed
            }
        };
    }
}
*/