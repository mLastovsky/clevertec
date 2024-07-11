package ru.clevertec.check.utils.Csv;

import ru.clevertec.check.entity.DiscountCardEntity;
import ru.clevertec.check.entity.ProductEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvConverter {
    public static List<ProductEntity> toProductEntities(List<Map<String, String>> data){
        List<ProductEntity> productEntities = new ArrayList<>();

        for (Map<String, String> row : data) {
            int id = Integer.parseInt(row.get("id"));
            String description = row.get("description");
            double price = Double.parseDouble(row.get("price"));
            int quantityInStock = Integer.parseInt(row.get("quantity in stock"));
            boolean wholesaleProduct = Boolean.parseBoolean(row.get("wholesale product"));

            productEntities.add(new ProductEntity(id, description, price, quantityInStock, wholesaleProduct));
        }

        return productEntities;
    }

    public static List<DiscountCardEntity> toDiscountCardEntities(List<Map<String, String>> data) {
        List<DiscountCardEntity> discountCardEntities = new ArrayList<>();

        for (Map<String, String> row : data) {
            int id = Integer.parseInt(row.get("id"));
            String number = row.get("number");
            double discountAmount = Double.parseDouble(row.get("discount amount"));
            discountCardEntities.add(new DiscountCardEntity(id, number, discountAmount));
        }

        return discountCardEntities;
    }

    public static List<Map<String, String>> fromProductEntities(List<ProductEntity> productEntities) {
        List<Map<String, String>> data = new ArrayList<>();

        for (ProductEntity product : productEntities) {
            Map<String, String> row = new HashMap<>();

            row.put("id", String.valueOf(product.getId()));
            row.put("description", product.getDescription());
            row.put("price", String.valueOf(product.getPrice()));
            row.put("quantity in stock", String.valueOf(product.getQuantityInStock()));
            row.put("wholesale product", String.valueOf(product.isWholesaleProduct()));

            data.add(row);
        }

        return data;
    }

    public static List<Map<String, String>> fromDiscountCardEntities(List<DiscountCardEntity> discountCardEntities) {
        List<Map<String, String>> data = new ArrayList<>();
        for (DiscountCardEntity discountCard : discountCardEntities) {
            Map<String, String> row = new HashMap<>();

            row.put("id", String.valueOf(discountCard.getId()));
            row.put("number", discountCard.getNumber());
            row.put("discount amount", String.valueOf(discountCard.getDiscountAmount()));

            data.add(row);
        }

        return data;
    }
}
