package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
class ValidationTest {

    UserController userController = new UserController();
    FilmController filmController = new FilmController();

    User user;
    Film film;

    @Test
    void userControllerTestBirthday() {
        user = new User(0, "Lohazavr@mail.ru",
                "GoldenRage",
                "Zurab",
                LocalDate.of(2024, 5, 10));
        try {
            userController.addUser(user);
        } catch (NotFoundException exception) {
            Assertions.assertEquals("Дата рождения указана в будущем", exception.getMessage());
        }
    }

    @Test
    void userControllerTestEmail() {
        user = new User(0, "Lohazavrmail.ru",
                "GoldenRage",
                "Zurab",
                LocalDate.of(2000, 5, 10));
        try {
            userController.addUser(user);
        } catch (NotFoundException exception) {
            Assertions.assertEquals("Емейл пустой или не содержит @", exception.getMessage());
        }
    }

    @Test
    void userControllerTestLogin() {
        user = new User(0, "Lohazavr@mail.ru",
                "G olde nR  age",
                "Zurab",
                LocalDate.of(2000, 5, 10));
        try {
            userController.addUser(user);
        } catch (NotFoundException exception) {
            Assertions.assertEquals("Логин содержит пробелы или пустой", exception.getMessage());
        }
    }

    @Test
    void filmControllerNameEmpty() {
        film = new Film(0, " ", "Блокбастер",
                LocalDate.of(2020, 10, 10), 100);
        try {
            filmController.addFilm(film);
        } catch (NotFoundException exception) {
            Assertions.assertEquals("Передано пустое имя фильма", exception.getMessage());
        }
    }

    @Test
    void filmControllerDescriptionMore200() {
        film = new Film(0, "Форсаж", "\"                      OVER 200                                               OVER 200                         \"+\n" +
                "                        \"                      OVER 200                                               OVER 200                         \"",
                LocalDate.of(2020, 10, 10), 100);
        try {
            filmController.addFilm(film);
        } catch (NotFoundException exception) {
            Assertions.assertEquals("Описание фильма превышает 200 символов", exception.getMessage());
        }
    }

    @Test
    void filmControllerBeforeBirthdayOfFilms() {
        film = new Film(0, "Форсаж", "Блокбастер",
                LocalDate.of(1893, 10, 10), 100);
        try {
            filmController.addFilm(film);
        } catch (NotFoundException exception) {
            Assertions.assertEquals("Дата релиза раньше чем рождение кино", exception.getMessage());
        }
    }

    @Test
    void filmControllerNegativeDuration() {
        film = new Film(0, "Форсаж", "Блокбастер",
                LocalDate.of(1921, 10, 10), -100);
        try {
            filmController.addFilm(film);
        } catch (NotFoundException exception) {
            Assertions.assertEquals("Отрицательная продолжительность фильма", exception.getMessage());
        }
    }
}
