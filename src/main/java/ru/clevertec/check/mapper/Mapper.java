package main.java.ru.clevertec.check.mapper;

public interface Mapper<F, T> {

    T mapFrom(F from);

    F mapTo(T to);

}
