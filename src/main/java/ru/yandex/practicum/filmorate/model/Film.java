package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.annotation.DateOverBirthday;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MpaRating;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults
public class Film {
    private Integer id;
    @NotBlank(message = "Пустое имя")
    String name;
    @NotBlank(message = "Пустое описание")
    @Size(max = 200, message = "Описание больше 200 символов")
    String description;
    @DateOverBirthday
    LocalDate releaseDate;
    @Positive(message = "Отрицательное значение продолжительности")
    Integer duration;
    Integer likes;
    List<Genre> genre;
    MpaRating mpa;

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
