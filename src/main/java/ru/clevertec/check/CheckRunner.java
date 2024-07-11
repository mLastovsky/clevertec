package ru.clevertec.check;

import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.service.ArgumentsParser;
import ru.clevertec.check.service.CheckService;

import java.util.List;

public class CheckRunner {
     public static void main(String[] args) throws NotEnoughMoneyException, BadRequestException {

         ArgumentsParser.Arguments arguments = ArgumentsParser.parseArguments(args);
         List<String> productIds = arguments.getProductIds();
         List<String> quantities = arguments.getQuantities();
         String discountCardNumber = arguments.getDiscountCardNumber();
         double balanceDebitCard = arguments.getBalanceDebitCard();

        CheckService checkService = new CheckService();
        checkService.processCheck(productIds, quantities, discountCardNumber, balanceDebitCard);
    }
}
