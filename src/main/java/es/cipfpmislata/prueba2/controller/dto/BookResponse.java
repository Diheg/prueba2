package es.cipfpmislata.prueba2.controller.dto;

// DTO de salida: los datos que la API devuelve en el JSON.
// Así el cliente no ve directamente las clases del dominio (Book y Author).
public class BookResponse {

    private Long id;
    private String name;
    private String isbn;
    private String genre;
    private String authorName;

    public BookResponse() {
    }

    public BookResponse(Long id, String name, String isbn, String genre, String authorName) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.genre = genre;
        this.authorName = authorName;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
