package main.java.ru.clevertec.check.mapper.csv;

import main.java.ru.clevertec.check.mapper.Mapper;

public interface CsvMapper<F, T> extends Mapper<F, T> {

    String COLUMN_SEPARATOR = ";";

}
