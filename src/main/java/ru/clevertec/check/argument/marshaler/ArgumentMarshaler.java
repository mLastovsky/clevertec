package main.java.ru.clevertec.check.argument.marshaler;

public interface ArgumentMarshaler {

    void addValue(Object... values);

    Object getArgumentValue();

}
