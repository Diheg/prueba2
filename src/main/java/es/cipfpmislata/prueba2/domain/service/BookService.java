package es.cipfpmislata.prueba2.domain.service;

import es.cipfpmislata.prueba2.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book create(Book book);

    Optional<Book> update(Long id, Book book);

    boolean delete(Long id);
}
