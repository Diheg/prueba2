package es.cipfpmislata.prueba2.persistence.dao;

import es.cipfpmislata.prueba2.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book insert(Book book);

    Book update(Book book);

    void deleteById(Long id);
}
