package main.java.ru.clevertec.check.repository.csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;

public interface Csv {

    int HEADER_ROW_INDEX = 1;

    default List<String> readCsvFile(Path path) throws IOException {
        return Files.readAllLines(path).stream()
                .skip(HEADER_ROW_INDEX)
                .toList();
    }

    default void writeCsvFile(Path path, List<String> rows) throws IOException {
        Files.write(path, rows, CREATE, TRUNCATE_EXISTING);
    }
}
