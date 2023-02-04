package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
