package main.java.ru.clevertec.check.mapper.csv;

import main.java.ru.clevertec.check.model.DiscountCard;

public class CsvDiscountCardMapper implements CsvMapper<String, DiscountCard> {

    private static final int ID_INDEX = 0;
    private static final int NUMBER_INDEX = 1;
    private static final int DISCOUNT_PERCENTAGE_INDEX = 2;

    @Override
    public DiscountCard mapFrom(String csvRow) {
        var fields = csvRow.split(COLUMN_SEPARATOR);

        return DiscountCard.builder()
                .id(Long.parseLong(fields[ID_INDEX]))
                .number(fields[NUMBER_INDEX])
                .discountAmount(Integer.valueOf(fields[DISCOUNT_PERCENTAGE_INDEX]))
                .build();
    }

    @Override
    public String mapTo(DiscountCard discountCard) {
        return String.join(
                COLUMN_SEPARATOR,
                String.valueOf(discountCard.getId()),
                discountCard.getNumber(),
                String.valueOf(discountCard.getDiscountAmount())
        );
    }
}
