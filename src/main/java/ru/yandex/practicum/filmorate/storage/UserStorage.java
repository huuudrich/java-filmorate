package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

public interface UserStorage {
    User addUser(@Valid @RequestBody User user) throws NotFoundException;

    User refreshUser(@Valid @RequestBody User user) throws NotFoundException;

    HashMap<Integer, User> getAllUsers();

    void addFriends(Integer id, Integer friendId);

    void removeFriends(Integer id, Integer friendId);

    List<User> getListOfFriends(Integer id);

    List<User> getListOfCommonFriends(Integer id, Integer otherId);

    User getUser(Integer id);
}
