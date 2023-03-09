package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MpaRating;

import java.util.List;

@Component
public interface MpaDao {
    List<MpaRating> getAllMpa();

    MpaRating getMpa(Integer id) throws NotFoundException;
}
