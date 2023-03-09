package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class ValidationTest {
    @Autowired
    UserController userController = new UserController(new UserService(new UserStorage() {
        @Override
        public User addUser(User user) throws NotFoundException {
            return null;
        }

        @Override
        public User refreshUser(User user) throws NotFoundException {
            return null;
        }

        @Override
        public HashMap<Integer, User> getAllUsers() {
            return null;
        }

        @Override
        public void addFriends(Integer id, Integer friendId) throws NotFoundException {

        }

        @Override
        public void removeFriends(Integer id, Integer friendId) throws NotFoundException {

        }

        @Override
        public List<User> getListOfFriends(Integer id) {
            return null;
        }

        @Override
        public List<User> getListOfCommonFriends(Integer id, Integer otherId) {
            return null;
        }

        @Override
        public User getUser(Integer id) throws NotFoundException {
            return null;
        }
    }));

    User user;

    @Test
    void userControllerTestBirthday() {
        user = new User("Lohazavr@mail.ru",
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
        user = new User("Lohazavrmail.ru",
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
        user = new User("Lohazavr@mail.ru",
                "G olde nR  age",
                "Zurab",
                LocalDate.of(2000, 5, 10));
        try {
            userController.addUser(user);
        } catch (NotFoundException exception) {
            Assertions.assertEquals("Логин содержит пробелы или пустой", exception.getMessage());
        }
    }
}
