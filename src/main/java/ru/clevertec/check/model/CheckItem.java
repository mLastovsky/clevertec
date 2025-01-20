package main.java.ru.clevertec.check.model;

import java.math.BigDecimal;
import java.util.Objects;

public class CheckItem {

    private final int quantityProduct;
    private final Product product;
    private final BigDecimal price;
    private final BigDecimal discount;
    private final BigDecimal total;

    private CheckItem(builder builder) {
        this.quantityProduct = builder.quantityProduct;
        this.product = builder.product;
        this.price = builder.price;
        this.discount = builder.discount;
        this.total = builder.total;
    }

    public static builder builder() {
        return new builder();
    }

    public static class builder {
        private int quantityProduct;
        private Product product;
        private BigDecimal price = BigDecimal.ZERO;
        private BigDecimal discount = BigDecimal.ZERO;
        private BigDecimal total = BigDecimal.ZERO;

        public builder product(Product product) {
            this.product = product;
            return this;
        }

        public builder quantityProduct(int quantityProduct) {
            this.quantityProduct = quantityProduct;
            return this;
        }

        public builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public builder discount(BigDecimal discount) {
            this.discount = discount;
            return this;
        }

        public builder total(BigDecimal total) {
            this.total = total;
            return this;
        }

        public CheckItem build() {
            return new CheckItem(this);
        }
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantityProduct() {
        return quantityProduct;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckItem checkItem = (CheckItem) o;
        return quantityProduct == checkItem.quantityProduct && Objects.equals(product, checkItem.product) && Objects.equals(price, checkItem.price) && Objects.equals(discount, checkItem.discount) && Objects.equals(total, checkItem.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantityProduct, price, discount, total);
    }

    @Override
    public String toString() {
        return "CheckItem{" +
                "product=" + product +
                ", quantityProduct=" + quantityProduct +
                ", totalPriceWithoutDiscount=" + price +
                ", discount=" + discount +
                ", finalPrice=" + total +
                '}';
    }
}
