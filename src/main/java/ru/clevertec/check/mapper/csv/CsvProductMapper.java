package main.java.ru.clevertec.check.mapper.csv;

import main.java.ru.clevertec.check.model.Product;

import java.math.BigDecimal;

public class CsvProductMapper implements CsvMapper<String, Product> {

    private static final int ID_INDEX = 0;
    private static final int DESCRIPTION_INDEX = 1;
    private static final int PRICE_INDEX = 2;
    private static final int QUANTITY_INDEX = 3;
    private static final int WHOLESALE_INDEX = 4;

    @Override
    public Product mapFrom(String csv) {
        var fields = csv.split(SEPARATOR);

        return Product.builder()
                .id(Long.parseLong(fields[ID_INDEX]))
                .description(fields[DESCRIPTION_INDEX])
                .price(new BigDecimal(fields[PRICE_INDEX]))
                .quantityInStock(Integer.parseInt(fields[QUANTITY_INDEX]))
                .wholesaleProduct(Boolean.parseBoolean(fields[WHOLESALE_INDEX]))
                .build();
    }

    @Override
    public String mapTo(Product product) {
        return String.join(SEPARATOR,
                String.valueOf(product.getId()),
                product.getDescription(),
                product.getPrice().toString(),
                String.valueOf(product.getQuantityInStock()),
                String.valueOf(product.isWholesaleProduct())
        );
    }
}
