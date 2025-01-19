package main.java.ru.clevertec.check.model;

import java.math.BigDecimal;
import java.util.Objects;

public class CheckItem {

    private final Product product;
    private final int quantityProduct;
    private final BigDecimal totalPriceWithoutDiscount;
    private final BigDecimal discount;
    private final BigDecimal finalPrice;

    private CheckItem(builder builder) {
        this.product = builder.product;
        this.quantityProduct = builder.quantityProduct;
        this.totalPriceWithoutDiscount = builder.totalPriceWithoutDiscount;
        this.discount = builder.discount;
        this.finalPrice = builder.finalPrice;
    }

    public static builder builder() {
        return new builder();
    }

    public static class builder {
        private Product product;
        private int quantityProduct;
        private BigDecimal totalPriceWithoutDiscount = BigDecimal.ZERO;
        private BigDecimal discount = BigDecimal.ZERO;
        private BigDecimal finalPrice = BigDecimal.ZERO;

        public builder product(Product product) {
            this.product = product;
            return this;
        }

        public builder quantityProduct(int quantityProduct) {
            this.quantityProduct = quantityProduct;
            return this;
        }

        public builder totalPriceWithoutDiscount(BigDecimal totalPriceWithoutDiscount) {
            this.totalPriceWithoutDiscount = totalPriceWithoutDiscount;
            return this;
        }

        public builder discount(BigDecimal discount) {
            this.discount = discount;
            return this;
        }

        public builder finalPrice(BigDecimal finalPrice) {
            this.finalPrice = finalPrice;
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

    public BigDecimal getTotalPriceWithoutDiscount() {
        return totalPriceWithoutDiscount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckItem checkItem = (CheckItem) o;
        return quantityProduct == checkItem.quantityProduct && Objects.equals(product, checkItem.product) && Objects.equals(totalPriceWithoutDiscount, checkItem.totalPriceWithoutDiscount) && Objects.equals(discount, checkItem.discount) && Objects.equals(finalPrice, checkItem.finalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantityProduct, totalPriceWithoutDiscount, discount, finalPrice);
    }

    @Override
    public String toString() {
        return "CheckItem{" +
                "product=" + product +
                ", quantityProduct=" + quantityProduct +
                ", totalPriceWithoutDiscount=" + totalPriceWithoutDiscount +
                ", discount=" + discount +
                ", finalPrice=" + finalPrice +
                '}';
    }
}
