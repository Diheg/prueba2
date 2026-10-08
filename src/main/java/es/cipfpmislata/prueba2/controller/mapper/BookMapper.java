package es.cipfpmislata.prueba2.controller.mapper;

import es.cipfpmislata.prueba2.controller.dto.BookRequest;
import es.cipfpmislata.prueba2.controller.dto.BookResponse;
import es.cipfpmislata.prueba2.domain.model.Author;
import es.cipfpmislata.prueba2.domain.model.Book;

// Convierte entre los DTO del controller y el modelo del dominio
public class BookMapper {

    public static Book toBook(BookRequest request) {
        return new Book(
                null,
                request.getName(),
                request.getIsbn(),
                request.getGenre(),
                new Author(request.getAuthorName()));
    }

    public static BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getName(),
                book.getIsbn(),
                book.getGenre(),
                book.getAuthor().getName());
    }
}
