package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MpaRating;

import java.util.List;
@Qualifier("mpaDaoImpl")
@Component
@Slf4j
public class MpaDaoImpl implements MpaDao {
    JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MpaRating> getAllMpa() {
        String sql = "SELECT id, name FROM Mpa";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MpaRating.class));
    }

    @Override
    public MpaRating getMpa(Integer id) throws NotFoundException {
        String sql = "SELECT id, name FROM Mpa WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(MpaRating.class));
        } catch (EmptyResultDataAccessException e) {
            String logMessage = "Mpa not found with id: " + id;
            log.warn(logMessage);
            throw new NotFoundException(logMessage);
        }
    }
}
