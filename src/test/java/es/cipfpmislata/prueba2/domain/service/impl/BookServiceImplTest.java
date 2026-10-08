package es.cipfpmislata.prueba2.domain.service.impl;

import es.cipfpmislata.prueba2.domain.model.Author;
import es.cipfpmislata.prueba2.domain.model.Book;
import es.cipfpmislata.prueba2.domain.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Test unitario: el repositorio es un "mock" (un objeto falso que controlamos),
// asi probamos solo la logica del servicio, sin base de datos
class BookServiceImplTest {

    private BookRepository bookRepository;
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    void create_quitaElIdAntesDeGuardar() {
        Book book = new Book(50L, "El Hobbit", "978-8445000663", "Fantasía", new Author("J. R. R. Tolkien"));
        when(bookRepository.save(book)).thenReturn(book);

        bookService.create(book);

        // Aunque nos manden un id, el libro nuevo se guarda sin id para que lo ponga la base de datos
        assertNull(book.getId());
        verify(bookRepository).save(book);
    }

    @Test
    void update_conLibroQueExiste_guardaConElIdDeLaUrl() {
        Book book = new Book(null, "El Quijote", "978-8424116491", "Clásico", new Author("Miguel de Cervantes"));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        Optional<Book> result = bookService.update(1L, book);

        assertTrue(result.isPresent());
        assertEquals(1L, book.getId());
    }

    @Test
    void update_conLibroQueNoExiste_devuelveVacioYNoGuarda() {
        Book book = new Book(null, "No existe", "000", "Nada", new Author("Nadie"));
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.update(999L, book);

        assertTrue(result.isEmpty());
        verify(bookRepository, never()).save(book);
    }

    @Test
    void delete_conLibroQueExiste_loBorraYDevuelveTrue() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));

        boolean deleted = bookService.delete(1L);

        assertTrue(deleted);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void delete_conLibroQueNoExiste_devuelveFalseYNoBorra() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        boolean deleted = bookService.delete(999L);

        assertFalse(deleted);
        verify(bookRepository, never()).deleteById(999L);
    }
}
