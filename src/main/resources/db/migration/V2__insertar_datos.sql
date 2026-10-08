INSERT INTO author (id, name) VALUES
    (1, 'Miguel de Cervantes'),
    (2, 'Gabriel García Márquez'),
    (3, 'George Orwell');

INSERT INTO book (id, name, isbn, genre, author_id) VALUES
    (1, 'Don Quijote de la Mancha', '978-8424116491', 'Novela', 1),
    (2, 'Cien años de soledad', '978-8497592208', 'Realismo mágico', 2),
    (3, '1984', '978-8499890944', 'Ciencia ficción', 3);
