package main.java.ru.clevertec.check.argument.marshaler;

import java.util.HashMap;
import java.util.Map;

public class ProductQuantityMarshaler implements ArgumentMarshaler{

    private Map<Integer, Integer> idQuantity = new HashMap<>();

    @Override
    public void addValue(Object... values) {
        int productId = (Integer) values[0];
        int quantity = (Integer) values[1];

        idQuantity.merge(productId, quantity, Integer::sum);
    }

    @Override
    public Object getArgumentValue() {
        return idQuantity;
    }
}
