package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class FilmService implements FilmStorage {

    private final FilmStorage filmDao;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmDao) {
        this.filmDao = filmDao;
    }


    @Override
    public Film addFilm(Film film) throws NotFoundException {
        return filmDao.addFilm(film);
    }

    @Override
    public Film refreshFilm(Film film) throws NotFoundException {
        return filmDao.refreshFilm(film);
    }

    @Override
    public HashMap<Integer, Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    @Override
    public void putLike(Integer id, Integer userId) throws NotFoundException {
        filmDao.putLike(id, userId);
    }

    @Override
    public void removeLike(Integer id, Integer userId) throws NotFoundException {
        filmDao.removeLike(id, userId);
    }

    @Override
    public List<Film> getListSortLikes(Integer count) {
        return filmDao.getListSortLikes(count);
    }

    @Override
    public Film getFilm(Integer id) throws NotFoundException {
        return filmDao.getFilm(id);
    }
}
