package main.java.ru.clevertec.check.parser.argument;

import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.parser.argument.marshaler.ArgumentMarshaler;
import main.java.ru.clevertec.check.parser.argument.marshaler.BalanceDebitCardMarshaler;
import main.java.ru.clevertec.check.parser.argument.marshaler.DiscountCardMarshaler;
import main.java.ru.clevertec.check.parser.argument.marshaler.ProductQuantityMarshaler;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class ArgsParser {

    private static final String ID_QUANTITY_REGEX_TEMPLATE = "\\d+-\\d+";
    private static final String DISCOUNT_CARD_REGEX_TEMPLATE = "discountCard=....";
    private static final String BALANCE_DEBIT_CARD_REGEX_TEMPLATE = "balanceDebitCard=[+-]?[0-9]+([.][0-9]+)?$";

    private final Map<String, ArgumentMarshaler> marshalers;

    public ArgsParser(String[] args) throws BadRequestException {
        this.marshalers = new HashMap<>();
        parseArguments(args);
    }

    private void parseArguments(String[] args) throws BadRequestException {
        for (String arg : args) {
            parseElement(arg.trim());
        }
    }

    private void parseElement(String element) throws BadRequestException {
        if (element.matches(ID_QUANTITY_REGEX_TEMPLATE)) {
            processIdQuantityField(element);

        } else if (element.matches(DISCOUNT_CARD_REGEX_TEMPLATE)) {
            processDiscountCard(element);

        } else if (element.matches(BALANCE_DEBIT_CARD_REGEX_TEMPLATE)) {
            processBalanceDebitCard(element);

        } else {
            throw new BadRequestException();
        }
    }

    private void processBalanceDebitCard(String element) {
        var balanceDebitCardMarshaler =
                marshalers.computeIfAbsent("balanceDebitCard", k -> new BalanceDebitCardMarshaler());

        var balanceDebitCard = Double.parseDouble(element.replace("balanceDebitCard=", ""));
        var tmp = BigDecimal.valueOf(balanceDebitCard);
        balanceDebitCardMarshaler.addValue(tmp);
    }

    private void processDiscountCard(String element) {
        var discountCardMarshaler =
                marshalers.computeIfAbsent("discountCard", k -> new DiscountCardMarshaler());

        var discountCard = Integer.parseInt(element.replace("discountCard=", ""));
        discountCardMarshaler.addValue(discountCard);
    }

    private void processIdQuantityField(String element) {
        var idQuantityMarshaler =
                marshalers.computeIfAbsent("productsQuantity", k -> new ProductQuantityMarshaler());

        var numbers = element.split(Pattern.quote("-"));
        var id = Long.parseLong(numbers[0]);
        var quantity = Integer.parseInt(numbers[1]);
        idQuantityMarshaler.addValue(id, quantity);
    }

    public Object getArgumentValueByNameOrDefault(String name, Object defaultValue) {
        return getMarshalerByName(name)
                .map(ArgumentMarshaler::getArgumentValue)
                .orElse(defaultValue);
    }

    private Optional<ArgumentMarshaler> getMarshalerByName(String name) {
        return Optional.ofNullable(marshalers.get(name));
    }
}
