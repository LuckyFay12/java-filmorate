package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class User {
    @Positive
    private Long id;
    private String name;
    @NotBlank(message = "Логин не должен быть пустым и содержать пробелов")
    private String login;
    @Email(message = "Некорректный формат электронной почты")
    @NotEmpty(message = "Электронная почта не может быть пустой")
    private String email;
    @PastOrPresent(message = "День рождение не может быть в будущем")
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();

    public User(Long id, String name, String login, String email, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
        this.birthday = birthday;
    }
}

