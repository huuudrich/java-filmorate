package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.List;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO Users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        int numRowsAffected = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        if (numRowsAffected == 1) {
            return user;
        } else {
            throw new RuntimeException("Failed to add user");
        }
    }

    @Override
    public User refreshUser(User user) {
        String sql = "UPDATE Users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public HashMap<Integer, User> getAllUsers() {
        String sql = "SELECT id, email, login, name, birthday FROM Users";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        HashMap<Integer, User> result = new HashMap<>();
        for (User user : users) {
            result.put(user.getId(), user);
        }
        return result;
    }

    @Override
    public void addFriends(Integer id, Integer friendId) {
        String sql = "INSERT INTO User_Friend (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void removeFriends(Integer id, Integer friendId) {
        String sql = "DELETE FROM User_Friend WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public List<User> getListOfFriends(Integer id) {
        String sql = "SELECT Users.id, Users.email, Users.login, Users.name, Users.birthday " +
                "FROM Users " +
                "JOIN User_Friend " +
                "ON Users.id = User_Friend.friend_id " +
                "WHERE User_Friend.user_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id);
    }

    @Override
    public List<User> getListOfCommonFriends(Integer id, Integer otherId) {
        String sql = "SELECT Users.id, Users.email, Users.login, Users.name, Users.birthday " +
                "FROM Users " +
                "JOIN User_Friend " +
                "ON Users.id = User_Friend.friend_id " +
                "WHERE User_Friend.user_id = ? " +
                "AND EXISTS (" +
                "  SELECT friend_id FROM User_Friend WHERE user_id = ? AND friend_id = User_Friend.friend_id" +
                ")";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id, otherId);
    }

    @Override
    public User getUser(Integer id) {
        String sql = "SELECT id, email, login, name, birthday FROM Users WHERE id = ?";
        List
    }
