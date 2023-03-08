package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.properties.Genre;

import java.util.List;

@Component
public interface GenreDao {
    List<Genre> getAllGenres();
    Genre getGenre(Integer id) throws NotFoundException;
}
