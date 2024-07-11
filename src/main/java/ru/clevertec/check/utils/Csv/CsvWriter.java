package ru.clevertec.check.utils.Csv;

import ru.clevertec.check.exception.InternalSeverErrorException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CsvWriter {
    public static void writeCsv(String filePath, List<Map<String,String>> data) throws InternalSeverErrorException {
        String separator = ";";
        try(FileWriter writer = new FileWriter(filePath)){

            if(!data.isEmpty()) {
                String headerLine = String.join(separator, data.getFirst().keySet());
                writer.write(headerLine + "\n");
            }else throw new InternalSeverErrorException();

            for(Map<String, String> row : data){
                String line = String.join(separator, row.values());
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
