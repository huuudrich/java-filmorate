package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@Validated
public class UserService implements UserStorage {
    UserStorage userDao;

    public UserService(@Qualifier("userDbStorage") UserStorage userDao) {
        this.userDao = userDao;
    }
    @Override
    public User addUser(User user) throws NotFoundException {
        return userDao.addUser(user);
    }
    @Override
    public User refreshUser(User user) throws NotFoundException {
        return userDao.refreshUser(user);
    }
    @Override
    public HashMap<Integer, User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void addFriends(Integer id, Integer friendId) throws NotFoundException {
        userDao.addFriends(id, friendId);
    }

    @Override
    public void removeFriends(Integer id, Integer friendId) throws NotFoundException {
        userDao.removeFriends(id, friendId);
    }

    @Override
    public List<User> getListOfFriends(Integer id) {
        return userDao.getListOfFriends(id);
    }

    @Override
    public List<User> getListOfCommonFriends(Integer id, Integer otherId) {
        return userDao.getListOfCommonFriends(id, otherId);
    }

    @Override
    public User getUser(Integer id) throws NotFoundException {
        return userDao.getUser(id);
    }
}
