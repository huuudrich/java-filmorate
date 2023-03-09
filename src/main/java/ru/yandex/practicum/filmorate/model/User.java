package ru.yandex.practicum.filmorate.model;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Integer id;
    @NotBlank(message = "Email пустой")
    @Email(message = "Неправильный email")
    private String email;
    @NotBlank(message = "Логин пустой")
    private String login;
    @NotEmpty(message = "Имя пустое")
    private String name;
    @Past(message = "Дата рождения указана в будущем")
    private LocalDate birthday;

    private HashSet<Integer> friends;

    @PostConstruct
    public void initFriends() {
        friends = new HashSet<>();
    }
}
