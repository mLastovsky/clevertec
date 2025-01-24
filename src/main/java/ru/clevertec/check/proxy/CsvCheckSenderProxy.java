package main.java.ru.clevertec.check.proxy;

import main.java.ru.clevertec.check.model.Check;
import main.java.ru.clevertec.check.model.CheckItem;
import main.java.ru.clevertec.check.util.Csv;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvCheckSenderProxy implements Csv, CheckSenderProxy {

    private static final Path DEFAULT_OUTPUT_FILE_PATH = Path.of("./src/main/result.csv");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void send(Check check) {
        var csvContent = buildCheckCsvContent(check);
        writeToFile(DEFAULT_OUTPUT_FILE_PATH, csvContent);
    }

    @Override
    public void sendError(String error) {
        var errorContent = buildErrorCsvContent(error);
        writeToFile(DEFAULT_OUTPUT_FILE_PATH, errorContent);
    }

    private List<String> buildCheckCsvContent(Check check) {
        List<String> rows = new ArrayList<>();

        rows.add(buildRow("Date", "Time"));
        rows.add(buildRow(check.getDate().format(DATE_FORMATTER), check.getTime().format(TIME_FORMATTER)));
        rows.add("");

        rows.add(buildRow("QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"));
        check.getItems().forEach(item -> rows.add(formatCheckItem(item)));
        rows.add("");

        if (check.getDiscountCard() != null) {
            rows.add(buildRow("DISCOUNT CARD", "DISCOUNT PERCENTAGE"));
            rows.add(buildRow(
                    String.valueOf(check.getDiscountCard().getNumber()),
                    String.format("%d%%", check.getDiscountCard().getDiscountAmount())
            ));
            rows.add("");
        }

        rows.add(buildRow("TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"));
        rows.add(buildRow(
                formatCurrency(check.getTotalPrice()),
                formatCurrency(check.getTotalDiscount()),
                formatCurrency(check.getTotalWithDiscount())
        ));

        return rows;
    }

    private List<String> buildErrorCsvContent(String error) {
        List<String> rows = new ArrayList<>();
        rows.add("ERROR");
        rows.add(error);
        return rows;
    }

    private void writeToFile(Path path, List<String> content) {
        try {
            writeCsvFile(path, content);
        } catch (IOException e) {
            System.err.printf("Failed to save to file '%s': %s%n", path, e.getMessage());
        }
    }

    private String buildRow(String... headers) {
        return String.join(SEPARATOR, headers);
    }

    private String formatCheckItem(CheckItem item) {
        return buildRow(
                String.valueOf(item.getQuantityProduct()),
                item.getProduct().getDescription(),
                formatCurrency(item.getPrice()),
                formatCurrency(item.getDiscount()),
                formatCurrency(item.getTotal())
        );
    }

    private String formatCurrency(BigDecimal amount) {
        return String.format("%.2f$", amount);
    }
}
