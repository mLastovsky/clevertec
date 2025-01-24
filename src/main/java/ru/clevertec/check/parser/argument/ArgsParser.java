package main.java.ru.clevertec.check.parser.argument;

import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.parser.argument.marshaler.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class ArgsParser {

    private static final String ID_QUANTITY_REGEX_TEMPLATE = "\\d+-\\d+";
    private static final String DISCOUNT_CARD_REGEX_TEMPLATE = "discountCard=....";
    private static final String BALANCE_DEBIT_CARD_REGEX_TEMPLATE = "balanceDebitCard=[+-]?[0-9]+([.][0-9]+)?$";
    private static final String PATH_TO_FILE_REGEX_TEMPLATE = "pathToFile=[\\s\\S]+";
    private static final String SAVE_TO_FILE_REGEX_TEMPLATE = "saveToFile=[\\s\\S]+";

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
            processProductQuantityField(element);

        } else if (element.matches(DISCOUNT_CARD_REGEX_TEMPLATE)) {
            processDiscountCard(element);

        } else if (element.matches(BALANCE_DEBIT_CARD_REGEX_TEMPLATE)) {
            processBalanceDebitCard(element);

        } else if (element.matches(PATH_TO_FILE_REGEX_TEMPLATE)) {
            processPathToFile(element);

        } else if (element.matches(SAVE_TO_FILE_REGEX_TEMPLATE)) {
            processSaveToFile(element);

        } else {
            throw new BadRequestException();
        }
    }

    private void processSaveToFile(String element) {
        var saveToFileMarshaler =
                marshalers.computeIfAbsent("saveToFile", k -> new SaveToFileMarshaler());

        var saveToFilePath = element.replace("saveToFile=", "");
        saveToFileMarshaler.addValue(saveToFilePath);
    }

    private void processPathToFile(String element) {
        var pathToFileMarshaler =
                marshalers.computeIfAbsent("pathToFile", k -> new PathToFileMarshaler());

        var pathToFilePath = element.replace("pathToFile=", "");
        pathToFileMarshaler.addValue(pathToFilePath);
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

    private void processProductQuantityField(String element) {
        var productQuantitiesMarshaler =
                marshalers.computeIfAbsent("productQuantities", k -> new ProductQuantityMarshaler());

        var numbers = element.split(Pattern.quote("-"));
        var id = Long.parseLong(numbers[0]);
        var quantity = Integer.parseInt(numbers[1]);
        productQuantitiesMarshaler.addValue(id, quantity);
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
