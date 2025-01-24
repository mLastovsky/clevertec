package main.java.ru.clevertec.check;

import main.java.ru.clevertec.check.proxy.ConsoleCheckSenderProxy;
import main.java.ru.clevertec.check.proxy.CsvCheckSenderProxy;
import main.java.ru.clevertec.check.repository.csv.CsvDiscountCardRepository;
import main.java.ru.clevertec.check.repository.csv.CsvProductRepository;
import main.java.ru.clevertec.check.service.CheckService;
import main.java.ru.clevertec.check.service.DiscountCardService;
import main.java.ru.clevertec.check.service.ProductService;
import main.java.ru.clevertec.check.service.TransactionService;

public class CheckRunner {

    public static void main(String[] args) {

        var csvDiscountCardRepository = new CsvDiscountCardRepository();
        var csvProductRepository = new CsvProductRepository();

        var discountCardService = new DiscountCardService(csvDiscountCardRepository);
        var productService = new ProductService(csvProductRepository);

        var consoleCheckSenderProxy = new ConsoleCheckSenderProxy();
        var csvCheckSenderProxy = new CsvCheckSenderProxy();

        var checkService = new CheckService(productService, discountCardService);
        var transactionService = new TransactionService(checkService, consoleCheckSenderProxy, csvCheckSenderProxy);

        transactionService.processTransaction(args);
    }
}
