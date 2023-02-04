package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Validated
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
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
