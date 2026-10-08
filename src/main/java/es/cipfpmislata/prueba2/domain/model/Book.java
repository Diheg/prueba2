package es.cipfpmislata.prueba2.domain.model;

public class Book {

    private Long id;
    private String name;
    private String isbn;
    private String genre;
    private Author author;

    public Book() {
    }

    public Book(Long id, String name, String isbn, String genre, Author author) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.genre = genre;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
