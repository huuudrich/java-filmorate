package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Validated
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;
    UserStorage userDao;

    public UserService(@Qualifier("userDbStorage") UserStorage userDao) {
        this.userDao = userDao;
    }

    public User addUser(User user) throws NotFoundException {
        return userDao.addUser(user);
    }

    public User refreshUser(User user) throws NotFoundException {
        return userDao.refreshUser(user);
    }

    public HashMap<Integer, User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void addFriends(Integer id, Integer friendId) {
        getUser(id).getFriends().add(friendId);
        getUser(friendId).getFriends().add(id);
    }

    public void removeFriends(Integer id, Integer friendId) {
        getUser(id).getFriends().remove(friendId);
        getUser(friendId).getFriends().remove(id);
    }

    public List<User> getListOfFriends(Integer id) {
        List<User> friends = new ArrayList<>();
        getUser(id).getFriends()
                .forEach(i -> friends.add(getUser(i)));
        return friends;
    }

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

    public User getUser(Integer id) {
        return inMemoryUserStorage.getAllUsers()
                .get(id);
    }
}
