package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

@Repository
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    public Film addFilm(Film film) {
        id = id + 1;
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен новый фильм: " + film);
        return film;
    }

    public Film refreshFilm(Film film) throws NotFoundException {
        if (films.containsKey(film.getId())) {
            log.info("Refreshing film with id {}: {}", film.getId(), film);
            films.put(film.getId(), film);
            return film;
        } else {
            log.warn("Invalid film id: " + film.getId());
            throw new NotFoundException("Film with id " + film.getId() + " does not exist.");
        }
    }

    public HashMap<Integer, Film> getAllFilms() {
        return films;
    }
}

