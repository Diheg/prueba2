package es.cipfpmislata.prueba2.controller;

import es.cipfpmislata.prueba2.controller.dto.BookRequest;
import es.cipfpmislata.prueba2.controller.dto.BookResponse;
import es.cipfpmislata.prueba2.controller.mapper.BookMapper;
import es.cipfpmislata.prueba2.domain.model.Book;
import es.cipfpmislata.prueba2.domain.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

// El controller recibe y devuelve DTO; con el servicio habla usando Book (el modelo del dominio)
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /api/books
    @GetMapping
    public List<BookResponse> findAll() {
        return bookService.findAll().stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    // GET /api/books/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
        Optional<Book> book = bookService.findById(id);
        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookMapper.toResponse(book.get()));
    }

    // POST /api/books
    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody BookRequest request) {
        Book created = bookService.create(BookMapper.toBook(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(BookMapper.toResponse(created));
    }

    // PUT /api/books/{id}
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @RequestBody BookRequest request) {
        Optional<Book> updated = bookService.update(id, BookMapper.toBook(request));
        if (updated.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookMapper.toResponse(updated.get()));
    }

    // DELETE /api/books/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (bookService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
