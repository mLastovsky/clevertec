package main.java.ru.clevertec.check.util.argument.marshaler;

import java.math.BigDecimal;

public class BalanceDebitCardMarshaler implements ArgumentMarshaler{

    private BigDecimal balanceDebitCard;

    @Override
    public void addValue(Object... values) {
        balanceDebitCard = (BigDecimal) values[0];
    }

    @Override
    public Object getArgumentValue() {
        return balanceDebitCard;
    }
}
