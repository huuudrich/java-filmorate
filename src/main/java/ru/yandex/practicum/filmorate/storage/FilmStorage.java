package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

public interface FilmStorage {
    Film addFilm(@Valid @RequestBody Film film) throws NotFoundException;

    Film refreshFilm(@Valid @RequestBody Film film) throws NotFoundException;

    HashMap<Integer, Film> getAllFilms();
}
