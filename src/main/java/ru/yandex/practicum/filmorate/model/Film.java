package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;

}
