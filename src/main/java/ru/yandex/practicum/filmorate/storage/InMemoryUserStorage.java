package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@Repository
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    public User addUser(User user) {
        id = id + 1;
        user.setId(id);
        users.put(id, user);
        log.info("Добавлен новый пользователь: " + user);
        return user;
    }

    public User refreshUser(User user) throws NotFoundException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Updated user data for user with id " + user.getId() + ": " + user);
            return user;
        } else {
            log.warn("Invalid user id for user " + user.getId());
            throw new NotFoundException("Invalid user id: " + user.getId());
        }
    }

    public HashMap<Integer, User> getAllUsers() {
        return users;
    }
}
