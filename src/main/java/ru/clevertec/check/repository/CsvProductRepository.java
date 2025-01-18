package main.java.ru.clevertec.check.repository;

import main.java.ru.clevertec.check.exception.RepositoryException;
import main.java.ru.clevertec.check.mapper.csv.CsvProductMapper;
import main.java.ru.clevertec.check.model.Product;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvProductRepository implements Csv, ProductRepository {

    private static final Path DEFAULT_PRODUCTS_FILE_PATH = Path.of("src", "main", "resources", "products.csv");
    private static final CsvProductMapper csvProductMapper = new CsvProductMapper();

    @Override
    public List<Product> findAll() {
        try {
            return readCsvFile(DEFAULT_PRODUCTS_FILE_PATH).stream()
                    .map(csvProductMapper::mapFrom)
                    .toList();

        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        try {
            return readCsvFile(DEFAULT_PRODUCTS_FILE_PATH).stream()
                    .map(csvProductMapper::mapFrom)
                    .filter(product -> product.getId().equals(id))
                    .findFirst();

        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(Product entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Product save(Product entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
