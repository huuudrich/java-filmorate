package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.MpaDaoImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MpaRating;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    FilmService filmService;
    GenreDaoImpl genreDao;
    MpaDaoImpl mpaDao;
    private final String path = "/films";

    public FilmController(FilmService filmService, GenreDaoImpl genreDao, MpaDaoImpl mpaDao) {
        this.filmService = filmService;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
    }

    @Validated
    @PostMapping(path)
    public Film addFilm(@Valid @RequestBody Film film) throws NotFoundException {
        setMpaAndGenreWithFilm(film);
        return filmService.addFilm(film);
    }

    @Validated
    @PutMapping(path)
    public Film refreshFilm(@Valid @RequestBody Film film) throws NotFoundException {
        setMpaAndGenreWithFilm(film);
        return filmService.refreshFilm(film);
    }

    @Validated
    @GetMapping(path)
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmService.getAllFilms().values());
    }

    @PutMapping(path + "/{id}/like/{userId}")
    public void putLike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundException {
        filmService.putLike(id, userId);
    }

    @DeleteMapping(path + "/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundException {
        filmService.removeLike(id, userId);
    }

    @GetMapping(path + "/popular")
    public List<Film> getListSortLikes(@RequestParam(defaultValue = "0") Integer count) {
        return filmService.getListSortLikes(count);
    }

    @GetMapping(path + "/{id}")
    public Film getFilm(@PathVariable Integer id) throws NotFoundException {
        if (filmService.getAllFilms().size() < id)
            throw new NotFoundException("id превышает количество фильмов");
        return filmService.getFilm(id);
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable Integer id) throws NotFoundException {
        return genreDao.getGenre(id);
    }

    @GetMapping("/mpa")
    public List<MpaRating> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public MpaRating getMpa(@PathVariable Integer id) throws NotFoundException {
        return mpaDao.getMpa(id);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("Ошибка валидации: " +
                e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("Ошибка валидации: " +
                e.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleValidationException(NotFoundException e) {
        return new ResponseEntity<>("NotFound Exception: " +
                e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private void setMpaAndGenreWithFilm(Film film) throws NotFoundException {
        List<Genre> filmGenres = film.getGenre();
        List<Genre> newGenres = new ArrayList<>();
        MpaRating filmMpa;
        try {
            for (Genre genre : filmGenres) {
                newGenres.add(genreDao.getGenre(genre.getId()));
            }
            film.setGenre(newGenres);
        } catch (NullPointerException e) {
            film.setGenre(null);
        }
        try {
            filmMpa = mpaDao.getMpa(film.getMpa().getId());
            film.setMpa(filmMpa);
        } catch (NullPointerException e) {
            film.setMpa(null);
        }
    }
}
