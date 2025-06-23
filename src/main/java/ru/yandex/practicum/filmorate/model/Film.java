package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotations.MinDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    @Positive
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Length(max = 200, message = "Длина описания должна быть не больше 200 символов")
    private String description;
    @MinDate(date = "1895-12-28", message = "Дата релиза фильма — не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;
    private RatingMPA mpa;
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();
    @Builder.Default
    private Set<Director> directors = new LinkedHashSet<>();
}
