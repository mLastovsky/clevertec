package main.java.ru.clevertec.check.parser.argument.marshaler;

public class DiscountCardMarshaler implements ArgumentMarshaler {

    private String discountCard;

    @Override
    public void addValue(Object... values) {
        discountCard = (String) values[0];
    }

    @Override
    public Object getArgumentValue() {
        return discountCard;
    }
}
