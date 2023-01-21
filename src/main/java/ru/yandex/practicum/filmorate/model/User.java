package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;

@Data
@Builder
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
