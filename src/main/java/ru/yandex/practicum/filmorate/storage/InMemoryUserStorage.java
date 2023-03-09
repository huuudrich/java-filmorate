package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer id = 0;
    @Override
    public User addUser(User user) {
        id = id + 1;
        user.setId(id);
        users.put(id, user);
        log.info("Добавлен новый пользователь: " + user);
        return user;
    }
    @Override
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
    @Override
    public HashMap<Integer, User> getAllUsers() {
        return users;
    }

    @Override
    public void addFriends(Integer id, Integer friendId) {
        getUser(id).getFriends().add(friendId);
        getUser(friendId).getFriends().add(id);
    }

    @Override
    public void removeFriends(Integer id, Integer friendId) {
        getUser(id).getFriends().remove(friendId);
        getUser(friendId).getFriends().remove(id);
    }

    @Override
    public List<User> getListOfFriends(Integer id) {
        List<User> friends = new ArrayList<>();
        getUser(id).getFriends()
                .forEach(i -> friends.add(getUser(i)));
        return friends;
    }

    @Override
    public List<User> getListOfCommonFriends(Integer id, Integer otherId) {
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> userIdFriends = getUser(id).getFriends();
        Set<Integer> otherIdFriends = getUser(otherId).getFriends();
        userIdFriends.retainAll(otherIdFriends);
        for (Integer friendId : userIdFriends) {
            commonFriends.add(getUser(friendId));
        }
        return commonFriends;
    }

    @Override
    public User getUser(Integer id) {
        return getAllUsers()
                .get(id);
    }
}
