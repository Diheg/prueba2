package es.cipfpmislata.prueba2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// MockMvc hace peticiones HTTP falsas al controller sin arrancar el servidor de verdad.
// Pasan por todas las capas hasta la base de datos de test.
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getBooks_devuelveLosTresLibros() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Don Quijote de la Mancha"));
    }

    @Test
    void getBook_conIdExistente_devuelveElLibro() throws Exception {
        mockMvc.perform(get("/api/books/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("1984"))
                .andExpect(jsonPath("$.authorName").value("George Orwell"))
                .andExpect(jsonPath("$.author").doesNotExist());
    }

    @Test
    void getBook_conIdQueNoExiste_devuelve404() throws Exception {
        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postBook_creaElLibroYDevuelve201() throws Exception {
        String json = """
                {
                  "name": "El Hobbit",
                  "isbn": "978-8445000663",
                  "genre": "Fantasía",
                  "authorName": "J. R. R. Tolkien"
                }
                """;

        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("El Hobbit"))
                .andExpect(jsonPath("$.authorName").value("J. R. R. Tolkien"));
    }

    @Test
    void putBook_modificaElLibro() throws Exception {
        String json = """
                {
                  "name": "El Quijote",
                  "isbn": "978-8424116491",
                  "genre": "Clásico",
                  "authorName": "Miguel de Cervantes"
                }
                """;

        mockMvc.perform(put("/api/books/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("El Quijote"));
    }

    @Test
    void deleteBook_conIdExistente_devuelve204() throws Exception {
        mockMvc.perform(delete("/api/books/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_conIdQueNoExiste_devuelve404() throws Exception {
        mockMvc.perform(delete("/api/books/999"))
                .andExpect(status().isNotFound());
    }
}
