package main.java.ru.clevertec.check.proxy;

import main.java.ru.clevertec.check.model.Check;
import main.java.ru.clevertec.check.model.CheckItem;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsoleCheckSenderProxy implements CheckSenderProxy {

    private static final int RECEIPT_WIDTH = 61;
    private static final String LINE_SEPARATOR = "-".repeat(RECEIPT_WIDTH);
    private static final String TOTALS_SEPARATOR = "=".repeat(RECEIPT_WIDTH);

    @Override
    public void send(Check check) {
        var checkView = buildCheckView(check);
        System.out.println(checkView);
    }

    @Override
    public void sendError(String error) {
        var errorView = buildErrorView(error);
        System.out.println(errorView);
    }

    private StringBuilder buildErrorView(String error) {
        return new StringBuilder()
                .append(LINE_SEPARATOR).append("\n")
                .append(error).append("\n")
                .append(LINE_SEPARATOR);
    }

    private StringBuilder buildCheckView(Check check) {
        var checkView = new StringBuilder();

        checkView.append("\n")
                .append(formatHeader(check)).append("\n")
                .append(LINE_SEPARATOR).append("\n")
                .append(formatItemHeader()).append("\n")
                .append(LINE_SEPARATOR).append("\n")
                .append(formatItems(check.getItems())).append("\n")
                .append(formatDiscountCard(check)).append("\n")
                .append(formatTotals(check));

        return checkView;
    }

    private String formatHeader(Check check) {
        var date = check.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        var time = check.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        return centerText(String.format("CASHIER#1 %s %s", date, time));
    }

    private String formatItemHeader() {
        return String.format("%-6s %-25s %-9s %-11s %-10s", "QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL");
    }

    private String formatItems(List<CheckItem> items) {
        var itemsView = new StringBuilder();
        for (CheckItem item : items) {
            itemsView.append(formatItem(item)).append("\n");
        }
        return itemsView.toString().trim();
    }

    private String formatItem(CheckItem item) {
        var itemView = new StringBuilder();
        var descriptionLines = splitText(item.getProduct().getDescription(), 25);

        for (int i = 0; i < descriptionLines.size(); i++) {
            if (i == 0) {
                itemView.append(String.format("%-6d %-25s $%-8.2f $%-10.2f $%-10.2f",
                        item.getQuantityProduct(),
                        descriptionLines.get(i),
                        item.getPrice(),
                        item.getDiscount(),
                        item.getTotal()));
            } else {
                itemView.append(String.format("%-6s %-25s %-9s %-11s %-10s",
                        "", descriptionLines.get(i), "", "", ""));
            }
            itemView.append("\n");
        }

        return itemView.toString().trim();
    }

    private StringBuilder formatDiscountCard(Check check) {
        if (check.getDiscountCard() == null) {
            return new StringBuilder();
        }
        return new StringBuilder()
                .append(LINE_SEPARATOR).append("\n")
                .append(String.format("%-50s %10s", "DISCOUNT CARD:", check.getDiscountCard().getNumber())).append("\n")
                .append(String.format("%-56s %d%%", "DISCOUNT PERCENTAGE:", check.getDiscountCard().getDiscountAmount()));
    }

    private StringBuilder formatTotals(Check check) {
        return new StringBuilder()
                .append(TOTALS_SEPARATOR).append("\n")
                .append(String.format("%-54s $%-10.2f", "TOTAL PRICE:", check.getTotalPrice())).append("\n")
                .append(String.format("%-54s $%-10.2f", "TOTAL DISCOUNT:", check.getTotalDiscount())).append("\n")
                .append(String.format("%-54s $%-10.2f", "TOTAL WITH DISCOUNT:", check.getTotalWithDiscount())).append("\n");
    }

    private List<String> splitText(String text, int maxLength) {
        var words = text.split(" ");
        var currentLine = new StringBuilder();
        var allLines = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxLength) {
                if (!currentLine.isEmpty()) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                allLines.append(currentLine).append("\n");
                currentLine = new StringBuilder(word);
            }
        }
        if (!currentLine.isEmpty()) {
            allLines.append(currentLine);
        }

        return List.of(allLines.toString().split("\n"));
    }

    private String centerText(String text) {
        var padding = (RECEIPT_WIDTH - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }
}
