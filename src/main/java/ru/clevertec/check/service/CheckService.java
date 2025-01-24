package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.exception.InternalServerError;
import main.java.ru.clevertec.check.model.Check;
import main.java.ru.clevertec.check.model.CheckItem;
import main.java.ru.clevertec.check.model.DiscountCard;
import main.java.ru.clevertec.check.model.Product;
import main.java.ru.clevertec.check.model.ProductQuantity;
import main.java.ru.clevertec.check.parser.argument.ArgsParser;
import main.java.ru.clevertec.check.proxy.CheckSenderProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    public void generateReports(String[] args) throws BadRequestException, InternalServerError {
        var check = createCheck(args);
        checkSenderProxy.send(check);
    }

    public Check createCheck(String[] args) throws BadRequestException, InternalServerError {
        var argsParser = new ArgsParser(args);
        var discountCardNumber = (Integer) argsParser.getArgumentValueByNameOrDefault("discountCard", null);
        var productQuantities = (List<ProductQuantity>) argsParser.getArgumentValueByNameOrDefault("productQuantities", List.of());
        var balanceDebitCard = (BigDecimal) argsParser.getArgumentValueByNameOrDefault("balanceDebitCard", BigDecimal.ZERO);

        DiscountCard discountCard = null;
        if (discountCardNumber != null) {
            discountCard = discountCardService.getDiscountCardByNumber(discountCardNumber)
                    .orElse(discountCardService.createDefaultDiscountCard(discountCardNumber));
        }

        var items = convertToCheckItems(productQuantities, discountCard);
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

    private List<CheckItem> convertToCheckItems(List<ProductQuantity> productQuantities, DiscountCard discountCard) throws BadRequestException, InternalServerError {
        List<CheckItem> checkItems = new ArrayList<>();
        for (var element : productQuantities) {
            checkItems.add(convertToCheckItem(element, discountCard));
        }
        return checkItems;
    }

    private CheckItem convertToCheckItem(ProductQuantity productQuantity, DiscountCard discountCard) throws BadRequestException, InternalServerError {
        var productId = productQuantity.getId();
        var quantity = productQuantity.getQuantity();
        var product = productService.getProductById(productId);

        if(product.getQuantityInStock() < quantity){
            throw new InternalServerError(
                    String.format("Insufficient stock for product ID %d. Requested: %d, Available: %d",
                    productId, quantity, product.getQuantityInStock()));
        }

        var totalDiscount = calculateDiscountsForCheckItem(product, quantity, discountCard);
        var totalPrice = calculateTotalPriceForCheckItem(product, quantity);

        return CheckItem.builder()
                .product(product)
                .quantityProduct(quantity)
                .price(product.getPrice())
                .discount(totalDiscount)
                .total(totalPrice)
                .build();
    }

    private BigDecimal calculateDiscountsForCheckItem(Product product, Integer quantity, DiscountCard discountCard) {
        var wholesaleDiscount = productService.calculateWholesaleDiscountIfApplicable(product, quantity);

        if (wholesaleDiscount.compareTo(BigDecimal.ZERO) > 0) {
            return wholesaleDiscount;
        }

        if(discountCard != null){
            return discountCardService.calculateCardDiscount(product, quantity, discountCard);
        }

        return BigDecimal.ZERO;
    }


    private BigDecimal calculateTotalPriceForCheckItem(Product product, Integer quantity) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal computeDiscount(List<CheckItem> items) {
        return items.stream()
                .map(CheckItem::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal computeTotalPrice(List<CheckItem> items) {
        return items.stream()
                .map(CheckItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
