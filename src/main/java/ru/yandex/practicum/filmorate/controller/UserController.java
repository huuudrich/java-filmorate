package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@Validated
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws NotFoundException {
        return userService.addUser(user);
    }

    @PutMapping
    public User refreshUser(@Valid @RequestBody User user) throws NotFoundException {
        return userService.refreshUser(user);
    }

    @Validated
    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userService.getAllUsers().values());
    }

    @Validated
    @PutMapping("{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") @Positive int id,
                           @PathVariable("friendId") @Positive int friendId) throws NotFoundException {
        userService.addFriends(id, friendId);
    }

    @Validated
    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFriends(@PathVariable @Positive int id,
                              @PathVariable @Positive int friendId) throws NotFoundException {
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
        if (userService.getAllUsers().size() < id)
            throw new NotFoundException("Id ?????????????????? ???????????????????? ??????????????????????????");
        return userService.getUser(id);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("???????????? ??????????????????: " +
                e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("???????????? ??????????????????: " +
                e.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(NotFoundException e) {
        return new ResponseEntity<>("NotFound Exception: " +
                e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
