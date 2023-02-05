package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

interface UserStorage {
    User addUser(@Valid @RequestBody User user) throws NotFoundException;
    User refreshUser(@Valid @RequestBody User user) throws NotFoundException;
    HashMap<Integer, User> getAllUsers();
}
