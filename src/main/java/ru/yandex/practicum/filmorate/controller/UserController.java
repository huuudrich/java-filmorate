package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    HashMap<Integer, User> users = new HashMap<>();
    private int uid = 0;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        uid = uid + 1;
        user.setId(uid);
        users.put(uid, validateUser(user));
        log.info("Добавлен новый пользователь: " + user);
        return user;
    }

    @PutMapping
    public User refreshUser(@Valid @RequestBody User user) throws ValidationException {
        if (!users.isEmpty()) {
            for (Map.Entry<Integer, User> entry : users.entrySet()) {
                if (entry.getKey() == user.getId()) {
                    users.put(user.getId(), validateUser(user));
                    log.info("Обновлены данные пользователя:  " + user);
                } else {
                    log.warn("Неправильный id пользователя");
                    throw new ValidationException("Неправильный id пользователя");
                }
            }
        } else {
            throw new ValidationException("Список пользователей пуст");
        }
        return user;
    }

    @GetMapping
    public HashMap<Integer, User> getAllUsers() {
        log.info("Получен список пользователей: " + users);
        return users;
    }

    private User validateUser(User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Емейл пустой или не содержит @");
            throw new ValidationException("Емейл пустой или не содержит @");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Логин содержит пробелы или пустой");
            throw new ValidationException("Логин содержит пробелы или пустой");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения указана в будущем");
            throw new ValidationException("Дата рождения указана в будущем");
        } else {
            return user;
        }
    }
}
