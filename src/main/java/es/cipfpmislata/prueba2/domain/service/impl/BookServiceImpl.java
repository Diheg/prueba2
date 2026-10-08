package es.cipfpmislata.prueba2.domain.service.impl;

import es.cipfpmislata.prueba2.domain.model.Book;
import es.cipfpmislata.prueba2.domain.repository.BookRepository;
import es.cipfpmislata.prueba2.domain.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book create(Book book) {
        book.setId(null);
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> update(Long id, Book book) {
        if (bookRepository.findById(id).isEmpty()) {
            return Optional.empty();
        }
        book.setId(id);
        return Optional.of(bookRepository.save(book));
    }

    @Override
    public boolean delete(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            return false;
        }
        bookRepository.deleteById(id);
        return true;
    }
}
