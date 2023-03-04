package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.DateOverBirthday;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.RatingMpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private Integer id;
    @NotBlank(message = "Пустое имя")
    private String name;
    @NotBlank(message = "Пустое описание")
    @Size(max = 200, message = "Описание больше 200 символов")
    private String description;
    @DateOverBirthday
    private LocalDate releaseDate;
    @Positive(message = "Отрицательное значение продолжительности")
    private Integer duration;
    private Set<Integer> likes;
    private Set<Genre> genres;
    private Set<RatingMpa> mpa;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
    }
}
