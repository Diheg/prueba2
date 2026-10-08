package es.cipfpmislata.prueba2.domain.repository;

import es.cipfpmislata.prueba2.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    void deleteById(Long id);
}
