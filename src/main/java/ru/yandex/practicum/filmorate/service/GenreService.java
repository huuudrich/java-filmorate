package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.properties.Genre;

import java.util.List;

@Service
public class GenreService implements GenreDao {
    GenreDao genreDao;

    public GenreService(@Qualifier("genreDaoImpl") GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    @Override
    public Genre getGenre(Integer id) throws NotFoundException {
        return genreDao.getGenre(id);
    }
}
