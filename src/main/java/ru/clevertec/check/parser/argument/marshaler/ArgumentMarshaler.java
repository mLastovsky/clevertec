package main.java.ru.clevertec.check.parser.argument.marshaler;

public interface ArgumentMarshaler {

    void addValue(Object... values);

    Object getArgumentValue();

}
