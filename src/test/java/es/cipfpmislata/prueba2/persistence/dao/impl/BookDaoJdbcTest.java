package es.cipfpmislata.prueba2.persistence.dao.impl;

import es.cipfpmislata.prueba2.domain.model.Author;
import es.cipfpmislata.prueba2.domain.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @Transactional deshace los cambios de cada test al terminar,
// asi la base de datos de test siempre empieza con los 3 libros de V2__insertar_datos.sql
@SpringBootTest
@Transactional
class BookDaoJdbcTest {

    @Autowired
    private BookDaoJdbc bookDao;

    @Test
    void findAll_devuelveLosTresLibrosIniciales() {
        List<Book> books = bookDao.findAll();

        assertEquals(3, books.size());
        assertEquals("Don Quijote de la Mancha", books.get(0).getName());
    }

    @Test
    void findById_conIdExistente_devuelveElLibroConSuAutor() {
        Optional<Book> book = bookDao.findById(3L);

        assertTrue(book.isPresent());
        assertEquals("1984", book.get().getName());
        assertEquals("978-8499890944", book.get().getIsbn());
        assertEquals("Ciencia ficción", book.get().getGenre());
        assertEquals("George Orwell", book.get().getAuthor().getName());
    }

    @Test
    void findById_conIdQueNoExiste_devuelveVacio() {
        Optional<Book> book = bookDao.findById(999L);

        assertTrue(book.isEmpty());
    }

    @Test
    void insert_conAutorNuevo_guardaElLibroYLeAsignaId() {
        Book book = new Book(null, "El Hobbit", "978-8445000663", "Fantasía", new Author("J. R. R. Tolkien"));

        Book saved = bookDao.insert(book);

        assertNotNull(saved.getId());
        Book found = bookDao.findById(saved.getId()).orElseThrow();
        assertEquals("El Hobbit", found.getName());
        assertEquals("J. R. R. Tolkien", found.getAuthor().getName());
    }

    @Test
    void insert_conAutorQueYaExiste_noDuplicaElAutor() {
        Book book = new Book(null, "Rebelión en la granja", "978-8499890951", "Fábula", new Author("George Orwell"));

        Book saved = bookDao.insert(book);

        assertEquals("George Orwell", bookDao.findById(saved.getId()).orElseThrow().getAuthor().getName());
        assertEquals(4, bookDao.findAll().size());
    }

    @Test
    void update_cambiaLosDatosDelLibro() {
        Book book = new Book(1L, "El Quijote", "978-8424116491", "Clásico", new Author("Miguel de Cervantes"));

        bookDao.update(book);

        Book found = bookDao.findById(1L).orElseThrow();
        assertEquals("El Quijote", found.getName());
        assertEquals("Clásico", found.getGenre());
    }

    @Test
    void deleteById_borraElLibro() {
        bookDao.deleteById(2L);

        assertTrue(bookDao.findById(2L).isEmpty());
        assertEquals(2, bookDao.findAll().size());
    }
}
