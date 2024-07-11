package ru.clevertec.check.service;

import ru.clevertec.check.exception.BadRequestException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentsParser {
    private static final Map<String, String> arguments = new HashMap<>();

    public static Arguments parseArguments(String[] args) throws BadRequestException {
        Arguments arguments = new Arguments();

        List<String> argumentsList = Arrays.asList(args);

        Pattern productPattern = Pattern.compile("(\\d+)-(\\d+)");
        Matcher matcher = productPattern.matcher(String.join(" ", argumentsList));
        while (matcher.find()) {
            String productId = String.valueOf(matcher.group(1));
            String quantity = String.valueOf(matcher.group(2));
            arguments.addProduct(productId, quantity);
        }

        Pattern keyValuePattern = Pattern.compile("(\\w+)=(\\w+)");
        matcher = keyValuePattern.matcher(String.join(" ", argumentsList));
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);

            switch (key) {
                case "discountCard":
                    arguments.setDiscountCardNumber(value);
                    break;
                case "balanceDebitCard":
                    arguments.setBalanceDebitCard(Double.parseDouble(value));
                    break;
                default:
                    throw new BadRequestException();
            }
        }

        return arguments;
    }

    public static class Arguments {
        private List<String> productIds = new ArrayList<>();
        private List<String> quantities = new ArrayList<>();
        private String discountCardNumber;
        private double balanceDebitCard;

        public void addProduct(String productId, String quantity) {
            productIds.add(productId);
            quantities.add(quantity);
        }

        public List<String> getProductIds() {
            return productIds;
        }

        public List<String> getQuantities() {
            return quantities;
        }

        public String getDiscountCardNumber() {
            return discountCardNumber;
        }

        public void setDiscountCardNumber(String discountCardNumber) {
            this.discountCardNumber = discountCardNumber;
        }

        public double getBalanceDebitCard() {
            return balanceDebitCard;
        }

        public void setBalanceDebitCard(double balanceDebitCard) {
            this.balanceDebitCard = balanceDebitCard;
        }
    }

}
