package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Positive
    private Long id;
    private String name;
    @NotBlank(message = "Логин не должен быть пустым и содержать пробелов")
    private String login;
    @Email
    @NotEmpty(message = "Электронная почта не может быть пустой")
    private String email;
    @PastOrPresent(message = "День рождение не может быть в будущем")
    private LocalDate birthday;

}

