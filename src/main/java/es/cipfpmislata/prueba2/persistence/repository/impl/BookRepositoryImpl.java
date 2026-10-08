package es.cipfpmislata.prueba2.persistence.repository.impl;

import es.cipfpmislata.prueba2.domain.model.Book;
import es.cipfpmislata.prueba2.domain.repository.BookRepository;
import es.cipfpmislata.prueba2.persistence.dao.BookDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final BookDao bookDao;

    public BookRepositoryImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookDao.findById(id);
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            return bookDao.insert(book);
        }
        return bookDao.update(book);
    }

    @Override
    public void deleteById(Long id) {
        bookDao.deleteById(id);
    }
}
