package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.model.DiscountCard;
import main.java.ru.clevertec.check.repository.DiscountCardRepository;

import java.util.List;
import java.util.Optional;

public class DiscountCardService {

    private static final int DEFAULT_DISCOUNT_AMOUNT = 2;

    private final DiscountCardRepository discountCardRepository;

    public DiscountCardService(DiscountCardRepository discountCardRepository) {
        this.discountCardRepository = discountCardRepository;
    }

    public List<DiscountCard> getDiscountCards() {
        return discountCardRepository.findAll();
    }

    public Optional<DiscountCard> getDiscountCardById(Long id) {
        return discountCardRepository.findById(id);
    }

    public DiscountCard getDiscountCardByNumber(String cardNumber) {
        return discountCardRepository.findByNumber(cardNumber)
                .orElseGet(() -> DiscountCard.builder()
                        .number(cardNumber)
                        .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
                        .build());
    }
}
