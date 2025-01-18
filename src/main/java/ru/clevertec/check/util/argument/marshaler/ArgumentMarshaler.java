package main.java.ru.clevertec.check.util.argument.marshaler;

public interface ArgumentMarshaler {

    void addValue(Object... values);

    Object getArgumentValue();

}
