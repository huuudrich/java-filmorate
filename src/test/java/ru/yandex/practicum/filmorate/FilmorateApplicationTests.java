package ru.yandex.practicum.filmorate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MpaRating;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDaoStorage userDao;
    private final FilmDaoStorage filmDao;
    private List<User> users = new ArrayList<>();

    @Test
    void createUserTest() throws NotFoundException {
        User userOne = new User()
                User userTwo
                        User userThree
        Optional<User> userOptional = Optional.ofNullable(userDao.getUser(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void updateUserTest() throws NotFoundException {
    }

    @Test
    void testGetAllUsers() {
    }

    @Test
    void testAddFriends() throws NotFoundException {
    }

    @Test
    void testRemoveFriends() throws NotFoundException {
    }

    @Test
    void testGetListOfFriends() {
    }

    @Test
    void testGetListOfCommonFriends() {
    }

    @Test
    void testGetUser() throws NotFoundException {
        Optional<User> userOptional = Optional.ofNullable(userDao.getUser(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void testAddFilm() throws NotFoundException {
    }

    @Test
    void testRefreshFilm() throws NotFoundException {
    }

    @Test
    void testGetAllFilms() {
    }

    @Test
    void testPutLike() throws NotFoundException {
    }

    @Test
    void testRemoveLike() throws NotFoundException {
    }

    @Test
    void testGetListSortLikes() {
    }

    @Test
    void testGetFilm() throws NotFoundException {
    }

    @Test
    void testGetAllGenres() {
    }

    @Test
    void testGetGenre() throws NotFoundException {
    }

    @Test
    void testGetAllMpa() {

    }

    @Test
    void testGetMpa() throws NotFoundException {

    }
}