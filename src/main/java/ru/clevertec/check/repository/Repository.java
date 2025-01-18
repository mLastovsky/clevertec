package main.java.ru.clevertec.check.repository;

import main.java.ru.clevertec.check.model.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<T extends Entity, ID extends Serializable> {

    List<T> findAll();

    Optional<T> findById(ID id);

    void update(T entity);

    T save(T entity);

    boolean delete(ID id);
}
