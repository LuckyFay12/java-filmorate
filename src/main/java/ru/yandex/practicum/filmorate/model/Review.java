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
// Содержимое отзыва. Не может быть пустым.
    @NotNull
    private Boolean isPositive; // Тип отзыва — негативный/положительный.
    @NotNull
    private Long filmId;
    @NotNull
    private Long userId;
    private Integer useful;
    // У отзыва имеется рейтинг. При создании отзыва рейтинг равен нулю.
    // Если пользователь оценил отзыв как полезный, это увеличивает его рейтинг на 1.
    // Если как бесполезный, то уменьшает на 1.Отзывы должны сортироваться по рейтингу полезности.
}
