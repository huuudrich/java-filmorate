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
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.MpaDaoImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MpaRating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
public class FilmController {

    private final FilmDao filmService;
    private final GenreDao genreDao;
    private final MpaDao mpaDao;
    private static final String PATH_FILMS = "/films";
    private static final String PATH_GENRE = "/genres";
    private static final String PATH_MPA = "/mpa";

    public FilmController(FilmService filmService, GenreDaoImpl genreDao, MpaDaoImpl mpaDao) {
        this.filmService = filmService;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
    }

    @Validated
    @PostMapping(PATH_FILMS)
    public Film addFilm(@Valid @RequestBody Film film) throws NotFoundException {
        return filmService.addFilm(film);

    }

    @Validated
    @PutMapping(PATH_FILMS)
    public Film refreshFilm(@Valid @RequestBody Film film) throws NotFoundException {
        return filmService.refreshFilm(film);
    }

    @Validated
    @GetMapping(PATH_FILMS)
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmService.getAllFilms().values());
    }

    @PutMapping(PATH_FILMS + "/{id}/like/{userId}")
    public void putLike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundException {
        filmService.putLike(id, userId);
    }

    @DeleteMapping(PATH_FILMS + "/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundException {
        filmService.removeLike(id, userId);
    }

    @GetMapping(PATH_FILMS + "/popular")
    public List<Film> getListSortLikes(@RequestParam(defaultValue = "0") Integer count) {
        return filmService.getListSortLikes(count);
    }

    @GetMapping(PATH_FILMS + "/{id}")
    public Film getFilm(@PathVariable Integer id) throws NotFoundException {
        if (!filmService.getAllFilms().containsKey(id))
            throw new NotFoundException("id превышает количество фильмов");
        return filmService.getFilm(id);
    }

    @GetMapping(PATH_GENRE)
    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    @GetMapping(PATH_GENRE + "/{id}")
    public Genre getGenre(@PathVariable Integer id) throws NotFoundException {
        return genreDao.getGenre(id);
    }

    @GetMapping(PATH_MPA)
    public List<MpaRating> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    @GetMapping(PATH_MPA + "/{id}")
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
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleValidationException(NotFoundException e) {
        return new ResponseEntity<>("NotFound Exception: " +
                e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
