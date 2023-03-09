package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Component
@Qualifier("userDbStorage")
@Slf4j
public class UserDaoStorage implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoStorage(JdbcTemplate jdbcTemplate) {
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
            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            user.setId(id);
            return user;
        } else {
            String logMessage = "Failed to add user" + user;
            log.warn(logMessage);
            throw new NotFoundException(logMessage);
        }
    }

    @Override
    public User refreshUser(User user) throws NotFoundException {
        if (getUser(user.getId()) == null) {
            String logMessage = "User with id " + user.getId() + " not found";
            log.warn(logMessage);
            throw new NotFoundException(logMessage);
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
        checkOnThrowAddAndRemoveFriends(id, friendId);

        String selectSql = "SELECT status FROM User_Friend WHERE user_id = ? AND friend_id = ?";
        List<Boolean> statusList = jdbcTemplate.queryForList(selectSql, Boolean.class, id, friendId);
        boolean areFriends = !statusList.isEmpty() && statusList.get(0);

        boolean status = !areFriends;

        if (areFriends) {
            String updateSql = "UPDATE User_Friend SET status = ? WHERE user_id = ? AND friend_id = ?";
            jdbcTemplate.update(updateSql, status, id, friendId);
        } else {
            String insertSql = "INSERT INTO User_Friend (user_id, friend_id, status) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, id, friendId, status);
        }
    }

    @Override
    public void removeFriends(Integer id, Integer friendId) throws NotFoundException {
        checkOnThrowAddAndRemoveFriends(id, friendId);
        String sql = "UPDATE User_Friend SET status = false WHERE user_id = ? AND friend_id = ?";
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
        String sql = "SELECT U.id, U.email, U.login, U.name, U.birthday " +
                "FROM Users U " +
                "JOIN User_Friend UF1 ON U.id = UF1.friend_id AND UF1.user_id = ? " +
                "JOIN User_Friend UF2 ON U.id = UF2.friend_id AND UF2.user_id = ? ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id, otherId);
    }

    @Override
    public User getUser(Integer id) throws NotFoundException {
        String sql = "SELECT id, email, login, name, birthday FROM Users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException e) {
            String logMessage = "User with id " + id + " not found";
            log.warn(logMessage);
            throw new NotFoundException(logMessage);
        }
    }

    private void checkOnThrowAddAndRemoveFriends(Integer id, Integer friendId) throws NotFoundException {
        if (getUser(id) == null) {
            String logMessage = "User not found with id: " + id;
            log.warn(logMessage);
            throw new NotFoundException(logMessage);
        } else if (getUser(friendId) == null) {
            String logMessage = "Friend not found with id: " + friendId;
            log.warn(logMessage);
            throw new NotFoundException(logMessage);
        }
    }
}
