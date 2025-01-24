package main.java.ru.clevertec.check.proxy;

import main.java.ru.clevertec.check.model.Check;
import main.java.ru.clevertec.check.model.CheckItem;
import main.java.ru.clevertec.check.util.Csv;

import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvCheckSenderProxy implements Csv, CheckSenderProxy {

    private static final Path DEFAULT_OUTPUT_FILE_PATH = Path.of("./src/main/result.csv");

    @Override
    public void send(Check check) {
        var outputPath = DEFAULT_OUTPUT_FILE_PATH;
        List<String> rows = new ArrayList<>();

        rows.add(String.join(SEPARATOR, "Date", "Time"));
        rows.add(String.join(SEPARATOR,
                check.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                check.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        ));
        rows.add("");

        rows.add(String.join(SEPARATOR, "QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"));

        for (CheckItem item : check.getItems()) {
            rows.add(String.join(SEPARATOR,
                    String.valueOf(item.getQuantityProduct()),
                    item.getProduct().getDescription(),
                    String.format("%.2f$", item.getPrice()),
                    String.format("%.2f$", item.getDiscount()),
                    String.format("%.2f$", item.getTotal())
            ));
        }
        rows.add("");

        if (check.getDiscountCard() != null) {
            rows.add(String.join(SEPARATOR, "DISCOUNT CARD", "DISCOUNT PERCENTAGE"));
            rows.add(String.join(SEPARATOR,
                    String.valueOf(check.getDiscountCard().getNumber()),
                    String.format("%d%%", check.getDiscountCard().getDiscountAmount())
            ));
            rows.add("");
        }

        rows.add(String.join(SEPARATOR, "TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"));
        rows.add(String.join(SEPARATOR,
                String.format("%.2f$", check.getTotalPrice()),
                String.format("%.2f$", check.getTotalDiscount()),
                String.format("%.2f$", check.getTotalWithDiscount())
        ));

        try {
            writeCsvFile(outputPath, rows);
        } catch (IOException e) {
            System.err.println("Failed to save check to file: " + e.getMessage());
        }
    }

    @Override
    public void sendError(String error) {
        var outputPath = DEFAULT_OUTPUT_FILE_PATH;
        List<String> errorRow = List.of("Error", error);

        try {
            writeCsvFile(outputPath, errorRow);
        } catch (IOException e) {
            System.err.println("Failed to save error to file: " + e.getMessage());
        }
    }
}
