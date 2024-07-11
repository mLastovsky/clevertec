package ru.clevertec.check.service;

import ru.clevertec.check.model.Check;

import java.text.SimpleDateFormat;

public class CheckFormatter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static String formatForConsole(Check check) {
        StringBuilder formattedCheck = new StringBuilder();

        formattedCheck.append("------------------------\n");
        formattedCheck.append("Check\n");
        formattedCheck.append("------------------------\n");
        formattedCheck.append("Date: ").append(DATE_FORMAT.format(check.getDate())).append("\n");
        formattedCheck.append("Time: ").append(TIME_FORMAT.format(check.getTime())).append("\n");

        for (CheckItem item : check.getItems()) {
            formattedCheck.append(item.getProduct().getDescription())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append(" - ");
            if (item.getDiscount() > 0) {
                if (item.getProduct().isWholesaleProduct()) {
                    formattedCheck.append("WHOLESALE DISCOUNT 10% - ");
                } else {
                    formattedCheck.append("DISCOUNT ").append(item.getDiscount()).append("% - ");
                }
            }
            formattedCheck.append(String.format("%.2f", item.calculateTotalCost())).append("\n");
        }

        formattedCheck.append("------------------------\n");
        formattedCheck.append("TOTAL: ").append(String.format("%.2f", check.getTotalPrice())).append("\n");
        formattedCheck.append("TOTAL DISCOUNT: ").append(String.format("%.2f", check.getTotalDiscount())).append("\n");
        formattedCheck.append("TOTAL WITH DISCOUNT: ").append(String.format("%.2f", check.getTotalWithDiscount())).append("\n");
        formattedCheck.append("------------------------\n");

        return formattedCheck.toString();
    }

    public static String formatForCsv(Check check) {
        StringBuilder formattedCheck = new StringBuilder();

        formattedCheck.append("DATE;TIME;DESCRIPTION;Количество;Скидка;PRICE\n");
        for (CheckItem item : check.getItems()) {
            formattedCheck.append(check.getDate().toString())
                    .append(";")
                    .append(check.getTime().toString())
                    .append(";")
                    .append(item.getProduct().getDescription())
                    .append(";")
                    .append(item.getQuantity())
                    .append(";")
                    .append(item.getDiscount())
                    .append(";")
                    .append(String.format("%.2f", item.calculateTotalCost()))
                    .append("\n");
        }

        return formattedCheck.toString();
    }
}
