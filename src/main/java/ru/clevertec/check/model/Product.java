package main.java.ru.clevertec.check.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product extends Entity {

    private final String description;
    private final BigDecimal price;
    private final int quantityInStock;
    private final boolean wholesaleProduct;

    private Product(builder builder) {
        super.id = builder.id;
        this.description = builder.description;
        this.price = builder.price;
        this.quantityInStock = builder.quantityInStock;
        this.wholesaleProduct = builder.wholesaleProduct;
    }

    public static builder builder() {
        return new builder();
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public boolean isWholesaleProduct() {
        return wholesaleProduct;
    }

    public static class builder {
        private Long id;
        private String description;
        private BigDecimal price;
        private int quantityInStock;
        private boolean wholesaleProduct;

        public builder description(String description) {
            this.description = description;
            return this;
        }

        public builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public builder quantityInStock(int quantityInStock) {
            this.quantityInStock = quantityInStock;
            return this;
        }

        public builder wholesaleProduct(boolean wholesaleProduct) {
            this.wholesaleProduct = wholesaleProduct;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return quantityInStock == product.quantityInStock && wholesaleProduct == product.wholesaleProduct && Objects.equals(description, product.description) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, price, quantityInStock, wholesaleProduct);
    }

    @Override
    public String toString() {
        return "Product{" +
                "description='" + description + '\'' +
                ", price=" + price +
                ", quantityInStock=" + quantityInStock +
                ", wholesaleProduct=" + wholesaleProduct +
                ", id=" + id +
                '}';
    }
}
