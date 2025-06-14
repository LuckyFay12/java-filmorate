package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private Long reviewId;
    @NotBlank
    private String content;
    @NotNull
    private Boolean isPositive; // Тип отзыва — негативный/положительный.
    @NotNull
    private Long filmId;
    @NotNull
    private Long userId;
    private Integer useful;
}
