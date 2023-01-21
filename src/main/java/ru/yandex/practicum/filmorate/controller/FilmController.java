package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    HashSet<Film> films = new HashSet<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        film.setId(film.getId() + 1);
        films.add(validateFilms(film));
        log.info("Добавлен новый фильм: " + film);
        return film;
    }

    @PutMapping
    public Film refreshFilm(@Valid @RequestBody Film film) throws ValidationException {
        for (Film f : films) {
            if (f.getId() == film.getId()) {
                log.info("Фильм обновлен: " + film);
                films.add(validateFilms(film));
            } else {
                log.warn("Неправильный id фильма");
                throw new ValidationException("Неправильный id фильма");
            }
        }
        return film;
    }

    @GetMapping
    public HashSet<Film> getAllFilms() {
        log.info("Получен список фильмов: " + films);
        return films;
    }

    private Film validateFilms(Film film) throws ValidationException {
        String emptyName = "Передано пустое имя фильма";
        String descriptionOver200 = "Описание фильма превышает 200 символов";
        String dateOverBirthday = "Дата релиза раньше чем рождение кино";
        String negativeDuration = "Отрицательная продолжительность фильма";
        final LocalDate birthdayFilms = LocalDate.of(1895, 12, 28);
        if (film.getName().isBlank()) {
            log.warn(emptyName);
            throw new ValidationException(emptyName);
        } else if (film.getDescription().length() > 200) {
            log.warn(descriptionOver200);
            throw new ValidationException(descriptionOver200);
        } else if (birthdayFilms.isAfter(film.getReleaseDate())) {
            log.warn(dateOverBirthday);
            throw new ValidationException(dateOverBirthday);
        } else if (film.getDuration() < 0) {
            log.warn(negativeDuration);
            throw new ValidationException(negativeDuration);
        } else {
            return film;
        }
    }
}
