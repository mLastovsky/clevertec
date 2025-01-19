package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.model.Product;
import main.java.ru.clevertec.check.repository.ProductRepository;

import java.util.List;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) throws BadRequestException {
        return productRepository.findById(id)
                .orElseThrow(
                        () -> new BadRequestException("Product not found")
                );
    }
}
