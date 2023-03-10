package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.FilmRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) throws NotFoundException {
        String sql = "INSERT INTO Films (name, description, release_date, duration, likes) " +
                "VALUES (?, ?, ?, ?, ?)";
        //int rowsAffected = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getDuration(), film.getLikes());
        int id = getIdAndUpdateFilm(sql, film);
        if (id != 0) {
            film.setId(id);
            return film;
        } else {
            throw new NotFoundException("Failed to add film to database");
        }

    }

    @Override
    public Film refreshFilm(Film film) throws NotFoundException {
        String sql = "UPDATE Films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, likes=? WHERE id = ?";
        int numRowsAffected = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getLikes(),
                film.getId()
        );
        if (numRowsAffected == 1) {
            return film;
        } else {
            throw new NotFoundException("Failed to update film in database");
        }
    }

    @Override
    public HashMap<Integer, Film> getAllFilms() {
        String sql = "SELECT f.*, m.name AS mpa_name, GROUP_CONCAT(DISTINCT g.name ORDER BY g.name SEPARATOR ', ') AS genres " +
                "FROM Films f " +
                "LEFT JOIN Film_Mpa fm ON f.id = fm.film_id " +
                "LEFT JOIN Mpa m ON fm.mpa_id = m.id " +
                "LEFT JOIN Film_Genre fg ON f.id = fg.film_id " +
                "LEFT JOIN Genres g ON fg.genre_id = g.id " +
                "GROUP BY f.id";
        List<Film> films = jdbcTemplate.query(sql, new FilmRowMapper());
        return films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film, (a, b) -> b, HashMap::new));
    }

    @Override
    public void putLike(Integer id, Integer userId) throws NotFoundException {
        String sql = "INSERT INTO Film_Like (user_id, film_id) VALUES (?, ?)";
        Film film = getFilm(id);
        int rowsAffected = jdbcTemplate.update(sql, userId, id);
        if (rowsAffected == 0) {
            throw new NotFoundException("Failed to put like in database");
        }
        film.setLikes(film.getLikes() + 1);
        refreshFilm(film);
    }

    @Override
    public void removeLike(Integer id, Integer userId) throws NotFoundException {
        String sql = "DELETE FROM Film_Like WHERE user_id = ? AND film_id = ?";
        getFilm(id).setLikes(getFilm(id).getLikes() - 1);
        int rowsAffected = jdbcTemplate.update(sql, userId, id);
        if (rowsAffected == 0) {
            throw new NotFoundException("Failed to remove like from database");
        }
    }

    @Override
    public List<Film> getListSortLikes(Integer count) {
        String sql = "SELECT *" +
                "FROM Films " +
                "GROUP BY id " +
                "ORDER BY likes DESC ";
        if (count == 0) {
            sql = sql + "LIMIT 10";
        } else {
            sql = sql + "LIMIT ?";
        }
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Film.class));
    }

    @Override
    public Film getFilm(Integer id) throws NotFoundException {
        String sql = "SELECT f.*, m.name AS mpa_name, GROUP_CONCAT(DISTINCT g.name ORDER BY g.name SEPARATOR ', ') AS genres " +
                "FROM Films f " +
                "LEFT JOIN Film_Mpa fm ON f.id = fm.film_id " +
                "LEFT JOIN Mpa m ON fm.mpa_id = m.id " +
                "LEFT JOIN Film_Genre fg ON f.id = fg.film_id " +
                "LEFT JOIN Genres g ON fg.genre_id = g.id " +
                "WHERE f.id = ? " +
                "GROUP BY f.id";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new FilmRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Film not found");
        }
    }


    private Integer getIdAndUpdateFilm(String sql, Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            if (film.getLikes() == null) {
                film.setLikes(0);
                ps.setInt(5, 0);
            } else {
                ps.setInt(5, film.getLikes());
            }
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }
}