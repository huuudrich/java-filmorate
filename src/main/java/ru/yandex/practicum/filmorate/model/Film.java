package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.DateOverBirthday;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MpaRating;

import java.time.LocalDate;
import java.util.List;

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
    private Integer likes;
    private List<Genre> genre;
    private MpaRating mpa;

    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, Integer duration, Integer likes, List<Genre> genre, MpaRating mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.genre = genre;
        this.mpa = mpa;
    }
}
