package ru.clevertec.check.entity;

import java.util.Objects;

public class DiscountCardEntity {
    private final int id;
    private final String number;
    private final double discountAmount;

    public DiscountCardEntity(int id, String number, double discountAmount) {
        this.id = id;
        this.number = number;
        this.discountAmount = discountAmount;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountCardEntity that = (DiscountCardEntity) o;
        return getId() == that.getId() && Double.compare(getDiscountAmount(), that.getDiscountAmount()) == 0 && Objects.equals(getNumber(), that.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumber(), getDiscountAmount());
    }

    @Override
    public String toString() {
        return "DiscountCardEntity{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", discountAmount=" + discountAmount +
                '}';
    }
}
