package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.configuration.DatabaseConfig;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@Validated
public class UserController {
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserService userService = new UserService(inMemoryUserStorage);

    UserDao userDao = new UserDao();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws NotFoundException {
        userDao.addUser(user);
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping
    public User refreshUser(@Valid @RequestBody User user) throws NotFoundException {
        userDao.refreshUser(user);
        return inMemoryUserStorage.refreshUser(user);
    }

    @Validated
    @GetMapping
    public List<User> getAllUsers() {
        userDao.getAllUsers();
        return new ArrayList<>(inMemoryUserStorage.getAllUsers().values());
    }

    @Validated
    @PutMapping("{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") @Positive int id,
                           @PathVariable("friendId") @Positive int friendId) {
        userService.addFriends(id, friendId);
    }

    @Validated
    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFriends(@PathVariable @Positive int id,
                              @PathVariable @Positive int friendId) {
        userService.removeFriends(id, friendId);
    }

    @Validated
    @GetMapping("{id}/friends")
    public List<User> getListOfFriends(@PathVariable @Positive int id) {
        return userService.getListOfFriends(id);
    }

    @Validated
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getListOfCommonFriends(@PathVariable @Positive int id,
                                             @PathVariable @Positive int otherId) {
        return userService.getListOfCommonFriends(id, otherId);
    }

    @Validated
    @GetMapping("{id}")
    public User getUser(@PathVariable("id") @Positive Integer id) throws NotFoundException {
        if (inMemoryUserStorage.getAllUsers().size() < id)
            throw new NotFoundException("Id превышает количество пользователей");
        return userService.getUser(id);
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(NotFoundException e) {
        return new ResponseEntity<>("NotFound Exception: " +
                e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
