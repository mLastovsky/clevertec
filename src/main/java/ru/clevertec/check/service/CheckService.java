package ru.clevertec.check.service;

import ru.clevertec.check.entity.DiscountCardEntity;
import ru.clevertec.check.entity.ProductEntity;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.model.Check;
import ru.clevertec.check.utils.Csv.CsvConverter;
import ru.clevertec.check.utils.Csv.CsvReader;

import java.util.List;

public class CheckService {
    public void processCheck(List<String> productIds, List<String> quantities, String discountCardNumber, double balance) throws NotEnoughMoneyException {
        String PRODUCTS_CSV = "./src/main/resources/products.csv";
        String DISCOUNT_CARD_CSV = "./src/main/resources/discountCards.csv";
        String RESULT_CSV = "./result.csv";

        List<ProductEntity> products = CsvConverter.toProductEntities(CsvReader.readCsv(PRODUCTS_CSV));
        List<DiscountCardEntity> discountCards = CsvConverter.toDiscountCardEntities(CsvReader.readCsv(DISCOUNT_CARD_CSV));

        Check check = new Check();

        for (int i = 0; i < productIds.size(); i++) {
            int productId = Integer.parseInt(productIds.get(i));
            int quantity = Integer.parseInt(quantities.get(i));
            products.stream()
                    .filter(p -> p.getId() == productId)
                    .findFirst().ifPresent(product -> check.addItem(product, quantity));
        }

        DiscountCardEntity discountCard = discountCards.stream()
                .filter(dc -> dc.getNumber().equals(discountCardNumber))
                .findFirst()
                .orElse(null);
        if (discountCard != null) {
            check.setDiscountCard(discountCard);
        }

        DiscountCalculator discountCalculator = new DiscountCalculator(discountCard);

        // Применение скидок к товарам в чеке
        for (CheckItem checkItem : check.getItems()) {
            // Рассчитываем скидку для каждого товара
            double discount = discountCalculator.calculateDiscount(checkItem.getProduct(), checkItem.getQuantity());
            checkItem.setDiscount(discount);
        }

        check.calculateTotal();

        if (check.getTotalWithDiscount() > balance) {
            throw new NotEnoughMoneyException();
        }

        String formattedCheck = CheckFormatter.formatForConsole(check);
        System.out.println(formattedCheck);

        check.saveToCsv();
    }
}
