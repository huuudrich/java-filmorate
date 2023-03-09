package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.*;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) throws NotFoundException {
        String sql = "INSERT INTO Users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, user.getBirthday() != null ? Date.valueOf(user.getBirthday()) : null, Calendar.getInstance(TimeZone.getTimeZone("UTC")));
            return ps;
        }, keyHolder);
        if (rowsAffected != 0) {
            int id = keyHolder.getKey().intValue();
            user.setId(id);
            return user;
        } else {
            throw new NotFoundException("Failed to add user");
        }
    }

    @Override
    public User refreshUser(User user) throws NotFoundException {
        if (getUser(user.getId()) == null) {
            throw new NotFoundException("User with id " + user.getId() + " not found");
        }
        String sql = "UPDATE Users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getUser(user.getId());
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
    public void addFriends(Integer id, Integer friendId) throws NotFoundException {
        if (getUser(id) == null) {
            throw new NotFoundException("User not found");
        } else if (getUser(friendId) == null) {
            throw new NotFoundException("Friend not found");
        }
        String sql = "INSERT INTO User_Friend (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void removeFriends(Integer id, Integer friendId) throws NotFoundException {
        if (getUser(id) == null) {
            throw new NotFoundException("User not found");
        } else if (getUser(friendId) == null) {
            throw new NotFoundException("Friend not found");
        }
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
    public User getUser(Integer id) throws NotFoundException {
        String sql = "SELECT id, email, login, name, birthday FROM Users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User not found with id: " + id);
        }
    }
}
