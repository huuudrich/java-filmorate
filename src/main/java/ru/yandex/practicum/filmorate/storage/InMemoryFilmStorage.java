package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    public Film addFilm(Film film) {
        id = id + 1;
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен новый фильм: " + film);
        return film;
    }

    public Film refreshFilm(Film film) throws NotFoundException {
        if (films.containsKey(film.getId())) {
            log.info("Refreshing film with id {}: {}", film.getId(), film);
            films.put(film.getId(), film);
            return film;
        } else {
            log.warn("Invalid film id: " + film.getId());
            throw new NotFoundException("Film with id " + film.getId() + " does not exist.");
        }
    }

    public HashMap<Integer, Film> getAllFilms() {
        return films;
    }

    @Override
    public void putLike(Integer id, Integer userId) throws NotFoundException {

    }

    @Override
    public void removeLike(Integer id, Integer userId) throws NotFoundException {

    }

    @Override
    public List<Film> getListSortLikes(Integer count) {
        return null;
    }

    @Override
    public Film getFilm(Integer id) throws NotFoundException {
        return null;
    }

    /*public void putLike(Integer id, Integer userId) {
        getFilm(id).getLikes().add(userId);
    }

    public void removeLike(Integer id, Integer userId) {
        getFilm(id).getLikes().remove(userId);
    }

    public Stream<Film> getListSortLikes(Integer count) {
        List<Film> sorted = new ArrayList<>(getAllFilms().values());
        return sorted.stream()
                .sorted(Comparator.comparingInt(o -> ((Film) o).getLikes().size()).reversed())
                .limit(count == 0 ? 10 : count);
    }

    public Film getFilm(Integer id) {
        return getAllFilms().get(id);
    }*/
}

