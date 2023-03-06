package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class User {
    private Integer id;
    @NotBlank(message = "Email пустой")
    @Email(message = "Непраивльный email")
    private String email;
    @NotBlank(message = "Логин пустой")
    private String login;
    @NotEmpty(message = "Имя пустое")
    private String name;
    private LocalDate birthday;

    private HashSet<Integer> friends;
    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }
}
