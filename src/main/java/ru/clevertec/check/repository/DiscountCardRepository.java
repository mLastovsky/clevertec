package main.java.ru.clevertec.check.repository;

import main.java.ru.clevertec.check.model.DiscountCard;

import java.util.Optional;

public interface DiscountCardRepository extends Repository<DiscountCard, Long> {

    Optional<DiscountCard> findByNumber(String cardNumber);

}
