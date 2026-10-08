# prueba2 — Aplicación de libros

API REST de libros hecha con Spring Boot para clase (2º de DAW, CIPFP Mislata).
Este archivo explica cómo está hecho el proyecto y las normas que hay que seguir al tocarlo,
tanto si lo lee una persona como un asistente de IA (opencode, Claude, Copilot...).

## Tecnologías

| Qué | Versión / detalle |
|---|---|
| Java | **25** |
| Spring Boot | 4.1.1 (Spring Web MVC) |
| Base de datos | H2 (archivo dentro del proyecto, en modo compatible con MySQL) |
| Acceso a datos | JDBC con `JdbcTemplate` (sin JPA ni Hibernate) |
| Migraciones | Flyway |
| Tests | JUnit 5, Mockito y MockMvc |
| Construcción | Maven (`mvnw`) |

## Normas del proyecto

Estas normas son obligatorias. Si eres un asistente de IA, **no las cambies aunque te parezca una mejora**.

1. **Java 25.** No bajes `<java.version>` en el `pom.xml` a 21 ni a otra versión.
2. **Nada de Lombok ni de Spring Validation.** Los getters, setters y constructores se escriben a mano.
3. **Nada de JPA ni de Spring Data.** El acceso a la base de datos se hace con `JdbcTemplate` y SQL escrito a mano.
4. **Código fácil de entender.** Es un proyecto de un alumno que lleva un mes en 2º de DAW: nada de trucos avanzados, comentarios cortos en español cuando algo no sea evidente.
5. **No cambies la estructura de paquetes** (ver más abajo). Es la que pide el profesor.
6. **No añadas dependencias al `pom.xml`** sin preguntar antes.
7. **TDD:** para cualquier funcionalidad nueva, primero se escribe el test, se comprueba que falla, y después se escribe el código que lo hace pasar.

## Estructura (arquitectura por capas)

```
src/main/java/es/cipfpmislata/prueba2
├── controller
│   ├── BookController              → recibe las peticiones HTTP (/api/books)
│   ├── dto
│   │   ├── BookRequest             → JSON que llega en POST y PUT (sin id)
│   │   └── BookResponse            → JSON que devuelve la API
│   └── mapper
│       └── BookMapper              → convierte DTO ⇄ Book
├── domain
│   ├── model
│   │   ├── Author                  → solo tiene: name
│   │   └── Book                    → id, name, isbn, genre, author
│   ├── repository
│   │   └── BookRepository          → interfaz que usa el servicio
│   └── service
│       ├── BookService             → interfaz con la lógica de negocio
│       └── impl
│           └── BookServiceImpl
├── persistence
│   ├── dao
│   │   ├── BookDao                 → interfaz con las operaciones SQL
│   │   └── impl
│   │       └── BookDaoJdbc         → implementación con JdbcTemplate
│   └── repository
│       └── impl
│           └── BookRepositoryImpl  → implementa BookRepository usando BookDao
└── Prueba2Application              → clase principal (main)
```

El camino de una petición es siempre:
**Controller → Service → Repository → DAO → base de datos H2**

Cada capa solo habla con la de debajo, nunca se salta ninguna.

### DTO (Data Transfer Object)

El controller **nunca recibe ni devuelve `Book` directamente**: usa DTO.

- `BookRequest`: lo que el cliente manda en el JSON. No tiene `id`.
- `BookResponse`: lo que la API devuelve. En vez del objeto `author`, lleva solo `authorName`.
- `BookMapper`: métodos estáticos `toBook(request)` y `toResponse(book)` para pasar de uno a otro.

Los DTO solo existen en la capa `controller`. Del servicio hacia abajo se trabaja siempre con `Book` y `Author`.

Para un endpoint nuevo: crea el DTO en `controller/dto`, añade su conversión en el mapper y úsalo en el controller.

## Base de datos

### Dónde está

La base de datos es **H2** y está guardada como un archivo dentro del proyecto:

| Base de datos | Dónde | Para qué se usa |
|---|---|---|
| `pruebaspring` | `data/pruebaspring.mv.db` | La de la aplicación |
| `pruebaspring_test` | En memoria (no se guarda en ningún archivo) | La de los tests: se crea al empezar y desaparece al terminar |

No hace falta instalar nada ni crearla a mano: H2 viene como dependencia en el `pom.xml`
y el archivo se crea solo la primera vez que se arranca la aplicación.
La carpeta `data/` está en el `.gitignore`: si se borra, Flyway la vuelve a crear con los datos iniciales.

La configuración está en:
- `src/main/resources/application.properties` → la aplicación
- `src/test/resources/application.properties` → los tests

Usuario: `sa`, sin contraseña. La URL lleva `MODE=MySQL` para que H2 entienda el SQL igual que MySQL.

### Ver la base de datos (consola H2)

Con la aplicación arrancada, abre en el navegador `http://localhost:8081/h2-console` y rellena:

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:file:./data/pruebaspring;MODE=MySQL` |
| User Name | `sa` |
| Password | (vacío) |

Pulsa **Connect**. A la izquierda salen las tablas; para ver datos escribe por ejemplo `SELECT * FROM book;` y pulsa **Run**.

### Tablas

```
author                         book
------                         ----
id    BIGINT (PK, auto)        id        BIGINT (PK, auto)
name  VARCHAR(100) UNIQUE      name      VARCHAR(150)
                               isbn      VARCHAR(20) UNIQUE
                               genre     VARCHAR(50)
                               author_id BIGINT (FK → author.id)
```

En Java, `Author` no tiene `id`, solo `name`. Por eso `BookDaoJdbc`, al guardar un libro,
busca el autor por su nombre y, si no existe, lo crea (método `findOrCreateAuthor`).

### Flyway (migraciones)

Flyway crea y modifica las tablas automáticamente al arrancar la aplicación.
Los scripts están en `src/main/resources/db/migration`:

| Script | Qué hace |
|---|---|
| `V1__crear_tablas.sql` | Crea las tablas `author` y `book` |
| `V2__insertar_datos.sql` | Inserta 3 autores y 3 libros de ejemplo |

Flyway apunta en la tabla `flyway_schema_history` qué scripts ya ha ejecutado, y no los vuelve a ejecutar.

**Normas de Flyway:**
- **Nunca modifiques un script que ya se ha ejecutado** (V1, V2...). Flyway detecta el cambio y la aplicación no arranca.
- Para cualquier cambio en la base de datos, crea un script nuevo con el siguiente número:
  `V3__descripcion_en_minusculas.sql` (dos guiones bajos después del número).
- **No crees ni modifiques tablas a mano desde la consola H2.** La consola solo se usa para ver los datos.

## API REST

Puerto: **8081**. Ruta base: `http://localhost:8081/api/books`

| Método | Anotación | Ruta | Qué hace | Respuesta |
|---|---|---|---|---|
| GET | `@GetMapping` | `/api/books` | Lista todos los libros | 200 |
| GET | `@GetMapping("/{id}")` | `/api/books/{id}` | Devuelve un libro | 200, o 404 si no existe |
| POST | `@PostMapping` | `/api/books` | Crea un libro | 201 |
| PUT | `@PutMapping("/{id}")` | `/api/books/{id}` | Modifica un libro | 200, o 404 si no existe |
| DELETE | `@DeleteMapping("/{id}")` | `/api/books/{id}` | Borra un libro | 204, o 404 si no existe |

Ejemplo de JSON para POST y PUT (`BookRequest`):

```json
{
  "name": "El Hobbit",
  "isbn": "978-8445000663",
  "genre": "Fantasía",
  "authorName": "J. R. R. Tolkien"
}
```

Respuesta (`BookResponse`): el mismo JSON con el `id` añadido.

Desde el navegador solo se pueden hacer los GET. Para POST, PUT y DELETE hace falta Postman o un archivo `.http` de IntelliJ.

## Tests

| Clase de test | Qué prueba | Cómo |
|---|---|---|
| `BookDaoJdbcTest` | El DAO con SQL de verdad | Base de datos H2 en memoria |
| `BookServiceImplTest` | La lógica del servicio | Con Mockito (repositorio falso, sin base de datos) |
| `BookControllerTest` | Los endpoints HTTP | Con MockMvc, pasando por todas las capas |
| `BookMapperTest` | La conversión entre DTO y `Book` | Test unitario normal |
| `Prueba2ApplicationTests` | Que la aplicación arranca | `@SpringBootTest` |

Los tests que usan la base de datos llevan `@Transactional`: todo lo que hacen se deshace al terminar,
así que cada test empieza siempre con los 3 libros de `V2__insertar_datos.sql`.

## Cómo arrancarlo

**Requisitos:** solo JDK 25. La base de datos H2 viene incluida en el proyecto.

En IntelliJ:
1. Abre la carpeta `prueba2` **de dentro** (la que tiene el `pom.xml`), no la de fuera.
2. Comprueba que el SDK es Java 25: **File → Project Structure → SDK**.
3. Si cambias el `pom.xml`, recarga Maven: **Ctrl + Shift + O**.
4. Ejecuta `Prueba2Application` con el botón ▶ verde.
5. Espera a que en la consola salga `Started Prueba2Application` y abre `http://localhost:8081/api/books`.

Desde la terminal (con `JAVA_HOME` apuntando a un JDK 25):

```bash
./mvnw test             # ejecuta todos los tests
./mvnw spring-boot:run  # arranca la aplicación
```

## Problemas frecuentes

| Error | Causa | Solución |
|---|---|---|
| `class file version 69.0 ... only recognizes up to 65.0` | Se está ejecutando con Java 21 en vez de Java 25 | Poner Java 25 como SDK del proyecto |
| `Port 8081 was already in use` | La aplicación ya está arrancada en otra ventana | Parar la otra con el botón ■ rojo |
| `Database may be already in use` | La aplicación ya está abierta en otra ventana y tiene el archivo de H2 ocupado | Parar la otra con el botón ■ rojo |
| La consola H2 sale vacía, sin tablas | La JDBC URL está mal escrita y H2 ha creado otra base de datos nueva | Poner exactamente `jdbc:h2:file:./data/pruebaspring;MODE=MySQL` |
| `Validate failed: Migration checksum mismatch` | Se ha modificado un script de Flyway que ya se había ejecutado | Dejar el script como estaba y hacer el cambio en un script nuevo |
| En el navegador: "no se puede acceder a este sitio" | La aplicación no está arrancada | Arrancarla desde IntelliJ |

## Documentación

- [Spring Boot 4.1.1](https://docs.spring.io/spring-boot/4.1.1/reference/)
- [Spring Web MVC](https://docs.spring.io/spring-boot/4.1.1/reference/web/servlet.html)
- [Acceso a datos con JdbcTemplate](https://docs.spring.io/spring-framework/reference/data-access/jdbc/core.html)
- [H2 Database](https://www.h2database.com/html/main.html)
- [Flyway con Spring Boot](https://docs.spring.io/spring-boot/4.1.1/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
- [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
- [Maven](https://maven.apache.org/guides/index.html)
