package es.cipfpmislata.prueba2.controller.mapper;

import es.cipfpmislata.prueba2.controller.dto.BookRequest;
import es.cipfpmislata.prueba2.controller.dto.BookResponse;
import es.cipfpmislata.prueba2.domain.model.Author;
import es.cipfpmislata.prueba2.domain.model.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookMapperTest {

    @Test
    void toBook_convierteElRequestEnUnLibroSinId() {
        BookRequest request = new BookRequest("El Hobbit", "978-8445000663", "Fantasía", "J. R. R. Tolkien");

        Book book = BookMapper.toBook(request);

        assertNull(book.getId());
        assertEquals("El Hobbit", book.getName());
        assertEquals("978-8445000663", book.getIsbn());
        assertEquals("Fantasía", book.getGenre());
        assertEquals("J. R. R. Tolkien", book.getAuthor().getName());
    }

    @Test
    void toResponse_convierteElLibroEnUnResponseConElNombreDelAutor() {
        Book book = new Book(3L, "1984", "978-8499890944", "Ciencia ficción", new Author("George Orwell"));

        BookResponse response = BookMapper.toResponse(book);

        assertEquals(3L, response.getId());
        assertEquals("1984", response.getName());
        assertEquals("978-8499890944", response.getIsbn());
        assertEquals("Ciencia ficción", response.getGenre());
        assertEquals("George Orwell", response.getAuthorName());
    }
}
