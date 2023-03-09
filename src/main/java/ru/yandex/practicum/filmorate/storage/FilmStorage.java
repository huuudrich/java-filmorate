package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
@Component
public interface FilmStorage {
    Film addFilm(@Valid @RequestBody Film film) throws NotFoundException;

    Film refreshFilm(@Valid @RequestBody Film film) throws NotFoundException;

    HashMap<Integer, Film> getAllFilms();

    void putLike(Integer id, Integer userId) throws NotFoundException;

    void removeLike(Integer id, Integer userId) throws NotFoundException;

    List<Film> getListSortLikes(Integer count);

    Film getFilm(Integer id) throws NotFoundException;
}
