
INSERT INTO Mpa (id, name)
SELECT * FROM (VALUES
                   (1, 'G'),
                   (2, 'PG'),
                   (3, 'PG-13'),
                   (4, 'R'),
                   (5, 'NC-17')
              ) AS g(id, name)
WHERE NOT EXISTS (SELECT 1 FROM Genres WHERE id = g.id);

INSERT INTO Genres (id, name)
SELECT * FROM (VALUES
                   (1, 'Комедия'),
                   (2, 'Драма'),
                   (3, 'Мультфильм'),
                   (4, 'Триллер'),
                   (5, 'Документальный'),
                   (6, 'Боевик')
              ) AS g(id, name)
WHERE NOT EXISTS (SELECT 1 FROM Genres WHERE id = g.id);