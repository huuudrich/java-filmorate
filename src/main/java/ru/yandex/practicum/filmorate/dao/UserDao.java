package ru.yandex.practicum.filmorate.dao;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

@Component
public interface UserDao {
    User addUser(@Valid @RequestBody User user) throws NotFoundException;

    User refreshUser(@Valid @RequestBody User user) throws NotFoundException;

    HashMap<Integer, User> getAllUsers();

    void addFriends(Integer id, Integer friendId) throws NotFoundException;

    void removeFriends(Integer id, Integer friendId) throws NotFoundException;

    List<User> getListOfFriends(Integer id);

    List<User> getListOfCommonFriends(Integer id, Integer otherId);

    User getUser(Integer id) throws NotFoundException;
}
