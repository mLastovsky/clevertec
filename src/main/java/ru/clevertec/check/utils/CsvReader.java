package ru.clevertec.check.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvReader {
    public static List<Map<String, String>> readCsv(String filePath) {
        List<Map<String, String>> data = new ArrayList<>();
        String separator = ";";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            String[] headers = reader.readLine().split(separator);
            while((line = reader.readLine()) != null){
                String[] values = line.split(separator);
                Map<String, String> row = new HashMap<>();

                for(int i = 0; i < headers.length; ++i){
                    row.put(headers[i].trim(),values[i].trim());
                }

                data.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }
}
