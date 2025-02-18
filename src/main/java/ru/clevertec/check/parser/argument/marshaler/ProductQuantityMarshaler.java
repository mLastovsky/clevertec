package main.java.ru.clevertec.check.parser.argument.marshaler;

import main.java.ru.clevertec.check.model.ProductQuantity;

import java.util.LinkedList;
import java.util.List;

public class ProductQuantityMarshaler implements ArgumentMarshaler{

    private final List<ProductQuantity> productQuantities = new LinkedList<>();

    @Override
    public void addValue(Object... values) {
        var productId = (Long) values[0];
        var quantity = (Integer) values[1];

        for(var entry : productQuantities) {
            if(entry.getId().equals(productId)) {
                entry.setQuantity(entry.getQuantity() + quantity);
                return;
            }
        }

        productQuantities.add(new ProductQuantity(productId, quantity));
    }

    @Override
    public Object getArgumentValue() {
        return productQuantities;
    }
}
