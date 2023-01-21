package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
        user = User.builder()
                .birthday(LocalDate.of(2023, 5, 10))
                .id(1)
                .email("Lohazavr@mail.ru")
                .login("GoldenRage")
                .name("Zurab").build();
        try {
            userController.addUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals("Дата рождения указана в будущем", exception.getMessage());
        }
    }

    @Test
    void userControllerTestEmail() {
        user = User.builder()
                .birthday(LocalDate.of(2000, 5, 10))
                .id(1)
                .email("Lohazavrmail.ru")
                .login("GoldenRage")
                .name("Zurab").build();
        try {
            userController.addUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals("Емейл пустой или не содержит @", exception.getMessage());
        }
    }

    @Test
    void userControllerTestLogin() {
        user = User.builder()
                .birthday(LocalDate.of(2000, 5, 10))
                .id(1)
                .email("Lohazavr@mail.ru")
                .login("Go ldenRa ge")
                .name("Zurab").build();
        try {
            userController.addUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals("Логин содержит пробелы или пустой", exception.getMessage());
        }
    }

    @Test
    void filmControllerNameEmpty() {
        film = Film.builder()
                .releaseDate(LocalDate.of(2020, 10, 10))
                .description("Блокбастер")
                .duration(100)
                .id(1)
                .name("").build();
        try {
            filmController.addFilm(film);
        } catch (ValidationException exception) {
            Assertions.assertEquals("Передано пустое имя фильма", exception.getMessage());
        }
    }

    @Test
    void filmControllerDescriptionMore200() {
        film = Film.builder()
                .releaseDate(LocalDate.of(2020, 10, 10))
                .description("Блокбастерdasdasdas[pdkasl;[kdkasl;dk;l'askdkasopiuwoujaoidhjsjdaklsdjakls" +
                        "jdlkjalskjdlkjasl;jdjlaksjdlkalskjdjlkajskl;djawo0ijudskloja" +
                        "kldjaskldjlaks;jlk;jacklv;kl;amwoiqhuyduihasjdnaslk.dnawoihpdhsakldhklsa")
                .duration(100)
                .id(1)
                .name("Форсаж 100").build();
        try {
            filmController.addFilm(film);
        } catch (ValidationException exception) {
            Assertions.assertEquals("Описание фильма превышает 200 символов", exception.getMessage());
        }
    }

    @Test
    void filmControllerBeforeBirthdayOfFilms() {
        film = Film.builder()
                .releaseDate(LocalDate.of(1893, 10, 10))
                .description("Блокбастер")
                .duration(100)
                .id(1)
                .name("Фосрааж 100000").build();
        try {
            filmController.addFilm(film);
        } catch (ValidationException exception) {
            Assertions.assertEquals("Дата релиза раньше чем рождение кино", exception.getMessage());
        }
    }

    @Test
    void filmControllerNegativeDuration() {
        film = Film.builder()
                .releaseDate(LocalDate.of(2000, 10, 10))
                .description("Блокбастер")
                .duration(-233)
                .id(1)
                .name("Фосрааж 100000").build();
        try {
            filmController.addFilm(film);
        } catch (ValidationException exception) {
            Assertions.assertEquals("Отрицательная продолжительность фильма", exception.getMessage());
        }
    }
}
