package ru.clevertec.check.service;

import ru.clevertec.check.entity.DiscountCardEntity;
import ru.clevertec.check.entity.ProductEntity;

public class DiscountCalculator {
    private final DiscountCardEntity discountCard;

    public DiscountCalculator(DiscountCardEntity discountCard) {
        this.discountCard = discountCard;
    }

    public double calculateDiscount(ProductEntity product, int quantity){
        double discount = 0;

        if (discountCard != null && !product.isWholesaleProduct() && quantity >= 5) {
            discount = discountCard.getDiscountAmount();
        }

        if (product.isWholesaleProduct() && quantity >= 5) {
            discount = 10;
        }

        return discount;
    }
}
