package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.exception.InternalServerError;
import main.java.ru.clevertec.check.model.Check;
import main.java.ru.clevertec.check.model.CheckItem;
import main.java.ru.clevertec.check.model.DiscountCard;
import main.java.ru.clevertec.check.model.Product;
import main.java.ru.clevertec.check.model.ProductQuantity;
import main.java.ru.clevertec.check.parser.argument.ArgsParser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CheckService {

    private final ProductService productService;
    private final DiscountCardService discountCardService;

    public CheckService(ProductService productService,
                        DiscountCardService discountCardService) {
        this.productService = productService;
        this.discountCardService = discountCardService;
    }

    public Check createCheck(ArgsParser argsParser) throws BadRequestException, InternalServerError {
        var discountCard = getDiscountCard(argsParser);
        var productQuantities = getProductQuantities(argsParser);

        var checkItems = createCheckItems(productQuantities, discountCard);

        return buildCheck(checkItems, discountCard);
    }

    private DiscountCard getDiscountCard(ArgsParser argsParser) {
        var discountCardNumber = (Integer) argsParser.getArgumentValueByNameOrDefault("discountCard", null);

        if (discountCardNumber != null) {
            return discountCardService.getDiscountCardByNumber(discountCardNumber)
                    .orElse(discountCardService.createDefaultDiscountCard(discountCardNumber));
        }

        return null;
    }

    private List<ProductQuantity> getProductQuantities(ArgsParser argsParser) throws BadRequestException {
        var productQuantities = (List<ProductQuantity>) argsParser.getArgumentValueByNameOrDefault("productQuantities", List.of());

        if (productQuantities.isEmpty()) {
            throw new BadRequestException("No products provided. At least one product is required.");
        }

        return productQuantities;
    }

    private List<CheckItem> createCheckItems(List<ProductQuantity> productQuantities, DiscountCard discountCard)
            throws BadRequestException, InternalServerError {

        List<CheckItem> checkItems = new ArrayList<>();
        for (ProductQuantity productQuantity : productQuantities) {
            checkItems.add(createCheckItem(productQuantity, discountCard));
        }
        return checkItems;
    }

    private CheckItem createCheckItem(ProductQuantity productQuantity, DiscountCard discountCard)
            throws BadRequestException, InternalServerError {

        var product = productService.getProductById(productQuantity.getId());
        validateStockAvailability(product, productQuantity.getQuantity());

        var totalDiscount = calculateItemDiscount(product, productQuantity.getQuantity(), discountCard);
        var totalPrice = calculateItemTotalPrice(product, productQuantity.getQuantity());

        return CheckItem.builder()
                .product(product)
                .quantityProduct(productQuantity.getQuantity())
                .price(product.getPrice())
                .discount(totalDiscount)
                .total(totalPrice)
                .build();
    }

    private void validateStockAvailability(Product product, Integer requestedQuantity) throws InternalServerError {
        if (product.getQuantityInStock() < requestedQuantity) {
            throw new InternalServerError(String.format(
                    "Insufficient stock for product ID %d. Requested: %d, Available: %d",
                    product.getId(), requestedQuantity, product.getQuantityInStock()
            ));
        }
    }

    private BigDecimal calculateItemDiscount(Product product, Integer quantity, DiscountCard discountCard) {
        var wholesaleDiscount = productService.calculateWholesaleDiscountIfApplicable(product, quantity);

        if (wholesaleDiscount.compareTo(BigDecimal.ZERO) > 0) {
            return wholesaleDiscount;
        }

        return discountCard != null
                ? discountCardService.calculateCardDiscount(product, quantity, discountCard)
                : BigDecimal.ZERO;
    }

    private BigDecimal calculateItemTotalPrice(Product product, Integer quantity) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    private Check buildCheck(List<CheckItem> checkItems, DiscountCard discountCard) {
        var totalPrice = computeTotalPrice(checkItems);
        var totalDiscount = computeTotalDiscount(checkItems);
        var totalWithDiscount = totalPrice.subtract(totalDiscount);

        return Check.builder()
                .date(LocalDate.now())
                .time(LocalTime.now())
                .items(checkItems)
                .discountCard(discountCard)
                .totalPrice(totalPrice)
                .totalDiscount(totalDiscount)
                .totalWithDiscount(totalWithDiscount)
                .build();
    }

    private BigDecimal computeTotalDiscount(List<CheckItem> items) {
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
