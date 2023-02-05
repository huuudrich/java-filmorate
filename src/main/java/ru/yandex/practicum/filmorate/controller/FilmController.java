package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(inMemoryFilmStorage);

    @Validated
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws NotFoundException {
        return inMemoryFilmStorage.addFilm(film);
    }

    @Validated
    @PutMapping
    public Film refreshFilm(@Valid @RequestBody Film film) throws NotFoundException {
        return inMemoryFilmStorage.refreshFilm(film);
    }

    @Validated
    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(inMemoryFilmStorage.getAllFilms().values());
    }

    @PutMapping("{id}/like/{userId}")
    public void putLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.putLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("popular")
    public List<Film> getListSortLikes(@RequestParam(defaultValue = "0") Integer count) {
        return filmService.getListSortLikes(count).toList();
    }

    @GetMapping("{id}")
    public Film getFilm(@PathVariable Integer id) throws NotFoundException {
        if (inMemoryFilmStorage.getAllFilms().size() < id)
            throw new NotFoundException("id превышает количество фильмов");
        return filmService.getFilm(id);
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
}
