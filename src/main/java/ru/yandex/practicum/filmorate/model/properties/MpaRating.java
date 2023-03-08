package ru.yandex.practicum.filmorate.model.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MpaRating {
    private Integer id;
    private String name;
}
