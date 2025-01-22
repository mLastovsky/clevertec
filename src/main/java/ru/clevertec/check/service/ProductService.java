package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.model.Product;
import main.java.ru.clevertec.check.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {

    private static final int WHOLESALE_THRESHOLD = 5;
    private static final BigDecimal WHOLESALE_DISCOUNT_PERCENT = BigDecimal.valueOf(10);

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

    public BigDecimal calculateWholesaleDiscountIfApplicable(Product product, Integer quantity) {
        if (isEligibleForWholesaleDiscount(product, quantity)) {
            return calculateWholesaleDiscount(product, quantity);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateWholesaleDiscount(Product product, Integer quantity) {
        var totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        return totalPrice.multiply(WHOLESALE_DISCOUNT_PERCENT).divide(BigDecimal.valueOf(100));
    }

    public boolean isEligibleForWholesaleDiscount(Product product, Integer quantity) {
        return product.isWholesaleProduct() && quantity >= WHOLESALE_THRESHOLD;
    }

}
