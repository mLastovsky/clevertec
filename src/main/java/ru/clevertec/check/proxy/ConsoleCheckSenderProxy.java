package main.java.ru.clevertec.check.proxy;

import main.java.ru.clevertec.check.model.Check;
import main.java.ru.clevertec.check.model.CheckItem;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConsoleCheckSenderProxy implements CheckSenderProxy {

    private static final int RECEIPT_WIDTH = 61;
    private static final String LINE_SEPARATOR = "-".repeat(RECEIPT_WIDTH);
    private static final String TOTALS_SEPARATOR = "=".repeat(RECEIPT_WIDTH);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void send(Check check) {
        System.out.println(buildCheckView(check));
    }

    @Override
    public void sendError(String error) {
        System.out.println(buildErrorView(error));
    }

    private String buildErrorView(String error) {
        return String.join("\n", LINE_SEPARATOR, error, LINE_SEPARATOR);
    }

    private String buildCheckView(Check check) {
        List<String> sections = new ArrayList<>();
        sections.add(formatHeader(check));
        sections.add(LINE_SEPARATOR);
        sections.add(formatItemHeader());
        sections.add(LINE_SEPARATOR);
        sections.add(formatItems(check.getItems()));
        sections.add(formatDiscountCard(check));
        sections.add(formatTotals(check));
        return String.join("\n", sections).trim();
    }

    private String formatHeader(Check check) {
        var date = check.getDate().format(DATE_FORMATTER);
        var time = check.getTime().format(TIME_FORMATTER);
        return centerText(String.format("CASHIER#1 %s %s", date, time));
    }

    private String formatItemHeader() {
        return String.format("%-6s %-25s %-9s %-11s %-10s", "QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL");
    }

    private String formatItems(List<CheckItem> items) {
        var itemsView = new StringBuilder();
        for (var item : items) {
            itemsView.append(formatItem(item)).append("\n");
        }
        return itemsView.toString().trim();
    }

    private String formatItem(CheckItem item) {
        var descriptionLines = splitText(item.getProduct().getDescription(), 25);
        var itemView = new StringBuilder();

        for (int i = 0; i < descriptionLines.size(); i++) {
            if (i == 0) {
                itemView.append(String.format("%-6d %-25s $%-8s $%-10s $%-10s",
                        item.getQuantityProduct(),
                        descriptionLines.get(i),
                        formatCurrency(item.getPrice()),
                        formatCurrency(item.getDiscount()),
                        formatCurrency(item.getTotal())));
            } else {
                itemView.append(String.format("%-6s %-25s %-9s %-11s %-10s",
                        "", descriptionLines.get(i), "", "", ""));
            }
            itemView.append("\n");
        }
        return itemView.toString().trim();
    }

    private String formatDiscountCard(Check check) {
        if (check.getDiscountCard() == null) {
            return "";
        }
        return String.join("\n",
                LINE_SEPARATOR,
                String.format("%-50s %10s", "DISCOUNT CARD:", check.getDiscountCard().getNumber()),
                String.format("%-56s %d%%", "DISCOUNT PERCENTAGE:", check.getDiscountCard().getDiscountAmount())
        );
    }

    private String formatTotals(Check check) {
        return String.join("\n",
                TOTALS_SEPARATOR,
                String.format("%-54s $%-10s", "TOTAL PRICE:", formatCurrency(check.getTotalPrice())),
                String.format("%-54s $%-10s", "TOTAL DISCOUNT:", formatCurrency(check.getTotalDiscount())),
                String.format("%-54s $%-10s", "TOTAL WITH DISCOUNT:", formatCurrency(check.getTotalWithDiscount()))
        );
    }

    private List<String> splitText(String text, int maxLength) {
        var words = text.split(" ");
        List<String> lines = new ArrayList<>();
        var currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxLength) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        return lines;
    }

    private String centerText(String text) {
        var padding = (RECEIPT_WIDTH - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    private String formatCurrency(BigDecimal amount) {
        return String.format("%.2f", amount);
    }
}
