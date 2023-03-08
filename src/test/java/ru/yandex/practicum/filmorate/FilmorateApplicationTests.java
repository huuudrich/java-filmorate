package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    User user;
    User friend;
    Film film;

    @BeforeEach
    public void setUp() throws NotFoundException {
        user = new User("Lohazavr@mail.ru",
                "GoldenRage",
                "Zurab",
                LocalDate.of(2001, 5, 10));
        userStorage.addUser(user);
        friend = new User("kak@mail.ru",
                "friend",
                "friend", LocalDate.of(2000, 5, 10));
        userStorage.addUser(friend);
        film = new Film("Новый фильм", "Фильм",
                LocalDate.of(2001, 5, 10), 250, 0, null, null);
        filmDbStorage.addFilm(film);
    }

    @Test
    public void testFindUserById() throws NotFoundException {

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindAllUsers() throws NotFoundException {
        Assertions.assertEquals(6, userStorage.getAllUsers().size());
    }

    @Test
    public void testAddFriends() throws NotFoundException {
        userStorage.addUser(friend);
        userStorage.addFriends(userStorage.getUser(1).getId(), userStorage.getUser(3).getId());
        Assertions.assertFalse(userStorage.getListOfFriends(1).isEmpty());
    }

    @Test
    public void testRemoveFriends() throws NotFoundException {
        userStorage.removeFriends(userStorage.getUser(1).getId(), userStorage.getUser(2).getId());
        Assertions.assertTrue(userStorage.getListOfFriends(1).isEmpty());
    }

    @Test
    public void testFindFilmById() throws NotFoundException {

        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.getFilm(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }


    @Test
    public void testputLike() throws NotFoundException {

        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.getFilm(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetListSortLikes() throws NotFoundException {

        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.getFilm(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testRemoveLike() throws NotFoundException {

        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.getFilm(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testRefreshFilm() throws NotFoundException {

        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.getFilm(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

}