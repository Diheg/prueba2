package es.cipfpmislata.prueba2.controller.dto;

// DTO de entrada: los datos que el cliente manda en el JSON al crear (POST) o modificar (PUT) un libro.
// No lleva id porque el id lo pone la base de datos (POST) o viene en la URL (PUT).
public class BookRequest {

    private String name;
    private String isbn;
    private String genre;
    private String authorName;

    public BookRequest() {
    }

    public BookRequest(String name, String isbn, String genre, String authorName) {
        this.name = name;
        this.isbn = isbn;
        this.genre = genre;
        this.authorName = authorName;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
