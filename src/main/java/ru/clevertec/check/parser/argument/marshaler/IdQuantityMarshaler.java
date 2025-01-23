package main.java.ru.clevertec.check.parser.argument.marshaler;

import java.util.HashMap;
import java.util.Map;

public class ProductQuantityMarshaler implements ArgumentMarshaler{

    private final Map<Long, Integer> productQuantity = new HashMap<>();

    @Override
    public void addValue(Object... values) {
        var productId = (Long) values[0];
        var quantity = (Integer) values[1];

        productQuantity.merge(productId, quantity, Integer::sum);
    }

    @Override
    public Object getArgumentValue() {
        return productQuantity;
    }
}
