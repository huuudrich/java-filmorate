package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;

public class FilmDao implements FilmStorage {

    @Override
    public Film addFilm(Film film) throws NotFoundException {
        return null;
    }

    @Override
    public Film refreshFilm(Film film) throws NotFoundException {
        return null;
    }

    @Override
    public HashMap<Integer, Film> getAllFilms() {
        return null;
    }
}
