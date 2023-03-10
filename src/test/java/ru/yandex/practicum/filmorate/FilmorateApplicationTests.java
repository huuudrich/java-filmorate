package ru.yandex.practicum.filmorate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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

    @BeforeClass
    public static void createUserTest() throws NotFoundException {
        User userOne = User.builder()
                .login("Kel").name("Alex")
                .birthday(LocalDate.of(2000, 10, 10))
                .email("polupoker@mail.ru")
                .friends(null).build();
        userDao.addUser(userOne);
        users.add(userOne);

        User userTwo = User.builder().id(1)
                .login("Peter").name("John")
                .birthday(LocalDate.of(1989, 3, 20))
                .email("john@yandex.ru")
                .friends(null).build();
        userDao.addUser(userTwo);
        users.add(userTwo);

        User userThree = User.builder()
                .login("Friend").name("Newest")
                .birthday(LocalDate.of(1960, 4, 14))
                .email("friend@gmail.com")
                .friends(null).build();
        userDao.addUser(userThree);
        users.add(userThree);

        Optional<User> userOptional = Optional.ofNullable(userDao.getUser(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void updateUserTest() throws NotFoundException {
        Optional<User> userOptional = Optional.ofNullable(userDao.refreshUser(users.get(1)));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "John")
                );
    }

    @Test
    void testGetAllUsers() {
        int size = userDao.getAllUsers().size();
        if (size != 0) {
            Assertions.assertEquals(size, users.size());
        } else {
            Assertions.fail("Список всех пользователей = 0");
        }
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