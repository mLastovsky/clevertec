package main.java.ru.clevertec.check.repository.csv;

import main.java.ru.clevertec.check.exception.RepositoryException;
import main.java.ru.clevertec.check.mapper.csv.CsvDiscountCardMapper;
import main.java.ru.clevertec.check.model.DiscountCard;
import main.java.ru.clevertec.check.repository.DiscountCardRepository;
import main.java.ru.clevertec.check.util.Csv;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class CsvDiscountCardRepository implements Csv, DiscountCardRepository {

    private static final Path DEFAULT_DISCOUNT_CARD_FILE_PATH = Path.of("./src/main/resources/discountCards.csv");
    private static final CsvDiscountCardMapper csvDiscountCardMapper = new CsvDiscountCardMapper();

    @Override
    public List<DiscountCard> findAll() {
        try {
            return readCsvFile(DEFAULT_DISCOUNT_CARD_FILE_PATH).stream()
                    .map(csvDiscountCardMapper::mapFrom)
                    .toList();

        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<DiscountCard> findById(Long id) {
        try {
            return readCsvFile(DEFAULT_DISCOUNT_CARD_FILE_PATH).stream()
                    .map(csvDiscountCardMapper::mapFrom)
                    .filter(product -> product.getId().equals(id))
                    .findFirst();

        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<DiscountCard> findByNumber(Integer number) {
        try {
            return readCsvFile(DEFAULT_DISCOUNT_CARD_FILE_PATH).stream()
                    .map(csvDiscountCardMapper::mapFrom)
                    .filter(product -> product.getNumber().equals(number))
                    .findFirst();

        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(DiscountCard entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DiscountCard save(DiscountCard entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(Long aLong) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
