package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.model.Check;
import main.java.ru.clevertec.check.model.CheckItem;
import main.java.ru.clevertec.check.model.DiscountCard;
import main.java.ru.clevertec.check.parser.argument.ArgsParser;
import main.java.ru.clevertec.check.proxy.CheckSenderProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class CheckService {

    private final ProductService productService;
    private final DiscountCardService discountCardService;
    private final CheckSenderProxy checkSenderProxy;

    public CheckService(ProductService productService,
                        DiscountCardService discountCardService,
                        CheckSenderProxy checkSenderProxy) {
        this.productService = productService;
        this.discountCardService = discountCardService;
        this.checkSenderProxy = checkSenderProxy;
    }

    public void generateReports(String[] args) throws BadRequestException {
        var check = createCheck(args);
        checkSenderProxy.send(check);
    }

    public Check createCheck(String[] args) throws BadRequestException {
        var argsParser = new ArgsParser(args);
        var discountCardNumber = (Integer) argsParser.getArgumentValueByNameOrDefault("discountCard", null);
        var idQuantityPairs = (Map<Long, Integer>) argsParser.getArgumentValueByNameOrDefault("idQuantityPairs", Map.of());
        var balanceDebitCard = (BigDecimal) argsParser.getArgumentValueByNameOrDefault("balanceDebitCard", BigDecimal.ZERO);

        DiscountCard discountCard = null;
        if (discountCardNumber != null) {
            discountCard = discountCardService.getDiscountCardByNumber(discountCardNumber)
                    .orElse(discountCardService.createDefaultDiscountCard(discountCardNumber));
        }

        var items = mapToCheckItems(idQuantityPairs, discountCard);
        var totalPrice = computeTotalPrice(items);
        var totalDiscount = computeDiscount(items);
        var totalWithDiscount = totalPrice.subtract(totalDiscount);

        return Check.builder()
                .date(LocalDate.now())
                .time(LocalTime.now())
                .items(items)
                .discountCard(discountCard)
                .totalPrice(totalPrice)
                .totalDiscount(totalDiscount)
                .totalWithDiscount(totalWithDiscount)
                .build();
    }

    private static BigDecimal computeDiscount(List<CheckItem> items) {
        return items.stream()
                .map(CheckItem::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal computeTotalPrice(List<CheckItem> items) {
        return items.stream()
                .map(CheckItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<CheckItem> mapToCheckItems(Map<Long, Integer> productsQuantity, DiscountCard discountCard) throws BadRequestException {
        return productsQuantity.entrySet().stream()
                .map(entry -> mapToCheckItem(discountCard, entry))
                .toList();
    }

    private CheckItem mapToCheckItem(DiscountCard discountCard, Map.Entry<Long, Integer> entry) {
        try {
            var productId = entry.getKey();
            var quantity = entry.getValue();
            var product = productService.getProductById(productId);

            var wholesaleDiscount = productService.isEligibleForWholesaleDiscount(product, quantity)
                    ? productService.calculateWholesaleDiscount(product, quantity)
                    : BigDecimal.ZERO;

            // Расчет скидки по карте, если оптовая скидка не применяется

            BigDecimal cardDiscount = BigDecimal.ZERO;
            if (discountCard != null) {
                cardDiscount = wholesaleDiscount.compareTo(BigDecimal.ZERO) > 0
                        ? BigDecimal.ZERO
                        : productService.calculateCardDiscount(product, quantity, discountCard.getDiscountAmount());
            }

            // Общая скидка и итоговая стоимость
            var totalDiscount = wholesaleDiscount.add(cardDiscount);
            var totalPrice = product.getPrice()
                    .multiply(BigDecimal.valueOf(quantity))
                    .subtract(totalDiscount);

            return CheckItem.builder()
                    .product(product)
                    .quantityProduct(quantity)
                    .price(product.getPrice())
                    .discount(totalDiscount)
                    .total(totalPrice)
                    .build();
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }
}
