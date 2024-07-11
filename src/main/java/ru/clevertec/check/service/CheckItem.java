package ru.clevertec.check.service;

import ru.clevertec.check.entity.ProductEntity;

public class CheckItem {

    private final ProductEntity product;
    private final int quantity;
    private double discount;

    public CheckItem(ProductEntity product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.discount = 0;
    }

    // Метод для расчета общей стоимости товара с учетом скидки
    public double calculateTotalCost() {
        double totalCost = product.getPrice() * quantity;
        totalCost -= (totalCost * discount / 100);
        return totalCost;
    }

    // Геттеры и сеттеры
    public ProductEntity getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
