package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class FilmService {
    InMemoryFilmStorage inMemoryFilmStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void putLike(Integer id, Integer userId) {
        getFilm(id).getLikes().add(userId);
    }

    public void removeLike(Integer id, Integer userId) {
        getFilm(id).getLikes().remove(userId);
    }

    public Stream<Film> getListSortLikes(Integer count) {
        List<Film> sorted = new ArrayList<>(inMemoryFilmStorage.getAllFilms().values());
        return sorted.stream()
                .sorted(Comparator.comparingInt(o -> ((Film) o).getLikes().size()).reversed())
                .limit(count == 0 ? 10 : count);
    }

    public Film getFilm(Integer id) {
        return inMemoryFilmStorage.getAllFilms().get(id);
    }
}
