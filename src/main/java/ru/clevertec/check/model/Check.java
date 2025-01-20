package main.java.ru.clevertec.check.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Check {

    private final LocalDate date;
    private final LocalTime time;
    private final List<CheckItem> items;
    private final DiscountCard discountCard;
    private final BigDecimal totalPrice;
    private final BigDecimal totalDiscount;
    private final BigDecimal totalWithDiscount;

    private Check(builder builder) {
        this.date = builder.date;
        this.time = builder.time;
        this.items = builder.items;
        this.discountCard = builder.discountCard;
        this.totalPrice = builder.totalPrice;
        this.totalDiscount = builder.totalDiscount;
        this.totalWithDiscount = builder.totalWithDiscount;
    }

    public static builder builder() {
        return new builder();
    }

    public static class builder {
        private LocalDate date;
        private LocalTime time;
        private List<CheckItem> items;
        private DiscountCard discountCard;
        private BigDecimal totalPrice = BigDecimal.ZERO;
        private BigDecimal totalDiscount = BigDecimal.ZERO;
        private BigDecimal totalWithDiscount = BigDecimal.ZERO;

        public builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public builder time(LocalTime time) {
            this.time = time;
            return this;
        }

        public builder items(List<CheckItem> items) {
            this.items = items;
            return this;
        }

        public builder discountCard(DiscountCard discountCard) {
            this.discountCard = discountCard;
            return this;
        }

        public builder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public builder totalDiscount(BigDecimal totalDiscount) {
            this.totalDiscount = totalDiscount;
            return this;
        }

        public builder totalWithDiscount(BigDecimal totalWithDiscount) {
            this.totalWithDiscount = totalWithDiscount;
            return this;
        }

        public Check build() {
            return new Check(this);
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public List<CheckItem> getItems() {
        return items;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public BigDecimal getTotalWithDiscount() {
        return totalWithDiscount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Check check = (Check) o;
        return Objects.equals(date, check.date) && Objects.equals(time, check.time) && Objects.equals(items, check.items) && Objects.equals(discountCard, check.discountCard) && Objects.equals(totalPrice, check.totalPrice) && Objects.equals(totalDiscount, check.totalDiscount) && Objects.equals(totalWithDiscount, check.totalWithDiscount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, items, discountCard, totalPrice, totalDiscount, totalWithDiscount);
    }

    @Override
    public String toString() {
        return "Check{" +
                "date=" + date +
                ", time=" + time +
                ", items=" + items +
                ", discountCard=" + discountCard +
                ", totalPrice=" + totalPrice +
                ", totalDiscount=" + totalDiscount +
                ", totalWithDiscount=" + totalWithDiscount +
                '}';
    }
}
