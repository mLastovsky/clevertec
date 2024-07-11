package ru.clevertec.check.model;

import ru.clevertec.check.entity.DiscountCardEntity;
import ru.clevertec.check.entity.ProductEntity;
import ru.clevertec.check.service.CheckItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Check {

    private final Date date;
    private final Date time;
    private final List<CheckItem> items;
    private DiscountCardEntity discountCard;
    private double totalPrice;
    private double totalDiscount;
    private double totalWithDiscount;

    public Check() {
        this.date = new Date();
        this.time = new Date();
        this.items = new ArrayList<>();
    }

    public void addItem(ProductEntity product, int quantity) {
        CheckItem checkItem = new CheckItem(product, quantity);
        items.add(checkItem);
    }

    public void setDiscountCard(DiscountCardEntity discountCard) {
        this.discountCard = discountCard;
    }

    // Расчет общей стоимости чека
    public void calculateTotal() {
        totalPrice = 0;
        totalDiscount = 0;

        for (CheckItem item : items) {
            totalPrice += item.calculateTotalCost();
            totalDiscount += item.getDiscount();
        }

        totalWithDiscount = totalPrice - totalDiscount;
    }

    // Метод для сохранения чека в файл (в CSV формате)
    public void saveToCsv() {

    }

    public Date getDate() {
        return date;
    }

    public Date getTime() {
        return time;
    }

    public List<CheckItem> getItems() {
        return items;
    }

    public DiscountCardEntity getDiscountCard() {
        return discountCard;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public double getTotalWithDiscount() {
        return totalWithDiscount;
    }
}
