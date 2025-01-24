package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.exception.InternalServerError;
import main.java.ru.clevertec.check.exception.NotEnoughMoneyException;
import main.java.ru.clevertec.check.model.Check;
import main.java.ru.clevertec.check.parser.argument.ArgsParser;
import main.java.ru.clevertec.check.proxy.CheckSenderProxy;

import java.math.BigDecimal;
import java.util.Arrays;

public class TransactionService {

    private final CheckService checkService;
    private final CheckSenderProxy[] checkSenders;

    public TransactionService(CheckService checkService, CheckSenderProxy... checkSenders) {
        this.checkService = checkService;
        this.checkSenders = checkSenders;
    }

    public void processTransaction(String[] args) {
        try {
            var argsParser = new ArgsParser(args);
            var debitCardBalance = getDebitCardBalance(argsParser);

            var check = checkService.createCheck(argsParser);
            validateBalance(debitCardBalance, check.getTotalWithDiscount());

            sendCheckToAllSources(check);
        } catch (BadRequestException e) {
            sendErrorToAllSources("BAD REQUEST");
            e.printStackTrace();
        } catch (InternalServerError e) {
            sendErrorToAllSources("INTERNAL SERVER ERROR");
            e.printStackTrace();
        } catch (NotEnoughMoneyException e) {
            sendErrorToAllSources("NOT ENOUGH MONEY");
            e.printStackTrace();
        }
    }

    private BigDecimal getDebitCardBalance(ArgsParser argsParser) throws BadRequestException {
        var debitCardBalance = (BigDecimal) argsParser.getArgumentValueByNameOrDefault("balanceDebitCard", null);
        if (debitCardBalance == null) {
            throw new BadRequestException("Debit card balance is not provided.");
        }
        return debitCardBalance;
    }

    private void validateBalance(BigDecimal debitCardBalance, BigDecimal totalCheckAmount) throws NotEnoughMoneyException {
        if (debitCardBalance.compareTo(totalCheckAmount) < 0) {
            throw new NotEnoughMoneyException("Insufficient funds: Balance = " + debitCardBalance + ", Required = " + totalCheckAmount);
        }
    }

    private void sendCheckToAllSources(Check check) {
        Arrays.stream(checkSenders).forEach(sender -> sender.send(check));
    }

    private void sendErrorToAllSources(String errorMessage) {
        Arrays.stream(checkSenders).forEach(sender -> sender.sendError(errorMessage));
    }
}
