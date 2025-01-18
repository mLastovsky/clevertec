package main.java.ru.clevertec.check.model;

import java.util.Objects;

public class DiscountCard extends Entity {

    private final String number;
    private final Integer discountAmount;

    private DiscountCard(builder builder) {
        super.id = builder.id;
        this.number = builder.number;
        this.discountAmount = builder.discountAmount;
    }

    public static builder builder() {
        return builder();
    }

    public String getNumber() {
        return number;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public static class builder {
        private Long id;
        private String number;
        private Integer discountAmount;

        public builder id(Long id) {
            this.id = id;
            return this;
        }

        public builder number(String number) {
            this.number = number;
            return this;
        }

        public builder discountAmount(Integer discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public DiscountCard build() {
            return new DiscountCard(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountCard that = (DiscountCard) o;
        return Objects.equals(number, that.number) && Objects.equals(discountAmount, that.discountAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, discountAmount);
    }

    @Override
    public String toString() {
        return "DiscountCard{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", discountAmount=" + discountAmount +
                '}';
    }
}
