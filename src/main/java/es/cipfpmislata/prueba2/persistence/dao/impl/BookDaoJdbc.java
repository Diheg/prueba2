package es.cipfpmislata.prueba2.persistence.dao.impl;

import es.cipfpmislata.prueba2.domain.model.Author;
import es.cipfpmislata.prueba2.domain.model.Book;
import es.cipfpmislata.prueba2.persistence.dao.BookDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class BookDaoJdbc implements BookDao {

    // Consulta base: cada libro junto con el nombre de su autor
    private static final String SELECT_BOOKS = """
            SELECT b.id, b.name, b.isbn, b.genre, a.name AS author_name
            FROM book b
            JOIN author a ON b.author_id = a.id
            """;

    private final JdbcTemplate jdbcTemplate;

    public BookDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query(SELECT_BOOKS + " ORDER BY b.id", this::mapBook);
    }

    @Override
    public Optional<Book> findById(Long id) {
        List<Book> books = jdbcTemplate.query(SELECT_BOOKS + " WHERE b.id = ?", this::mapBook, id);
        return books.stream().findFirst();
    }

    @Override
    public Book insert(Book book) {
        Long authorId = findOrCreateAuthor(book.getAuthor().getName());

        // KeyHolder recoge el id que MySQL genera con AUTO_INCREMENT
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO book (name, isbn, genre, author_id) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getName());
            ps.setString(2, book.getIsbn());
            ps.setString(3, book.getGenre());
            ps.setLong(4, authorId);
            return ps;
        }, keyHolder);

        book.setId(keyHolder.getKey().longValue());
        return book;
    }

    @Override
    public Book update(Book book) {
        Long authorId = findOrCreateAuthor(book.getAuthor().getName());
        jdbcTemplate.update(
                "UPDATE book SET name = ?, isbn = ?, genre = ?, author_id = ? WHERE id = ?",
                book.getName(), book.getIsbn(), book.getGenre(), authorId, book.getId());
        return book;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }

    // Busca el autor por su nombre y, si no existe, lo crea. Devuelve su id.
    private Long findOrCreateAuthor(String name) {
        List<Long> ids = jdbcTemplate.queryForList("SELECT id FROM author WHERE name = ?", Long.class, name);
        if (!ids.isEmpty()) {
            return ids.get(0);
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO author (name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    // Convierte una fila del resultado de la consulta en un objeto Book
    private Book mapBook(ResultSet rs, int rowNum) throws SQLException {
        return new Book(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("isbn"),
                rs.getString("genre"),
                new Author(rs.getString("author_name")));
    }
}
