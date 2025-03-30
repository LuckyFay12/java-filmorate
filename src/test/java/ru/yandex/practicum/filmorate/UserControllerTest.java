package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void init() {
        userController = new UserController();
    }

    @Test
    void createUserTest() {
        User user = new User(1L, "testName", "testLogin", "user@mail.ru", LocalDate.of(2000, 12, 12));
        userController.createUser(user);

        assertEquals(user.toString(), "User(id=1, name=testName, login=testLogin, email=user@mail.ru, birthday=2000-12-12)");
    }

    @Test
    void ifNameIsEmptyTheLoginWillBeUsed() {
        User user = new User(1L, "", "testLogin", "user@mail.ru", LocalDate.of(2000, 12, 12));
        userController.createUser(user);

        assertEquals("testLogin", user.getLogin(), "Вместо имени должен быть использован логин");
    }

    @Test
    void updateUserTest() {
        User user = new User(1L, "testName", "testLogin", "user@mail.ru", LocalDate.of(2000, 12, 12));
        userController.createUser(user);
        user.setName("Name1");
        userController.updateUser(user);

        assertEquals("Name1", user.getName(), "Имя должно обновиться");
    }

    @Test
    void getAllUsersTest() {
        User user1 = new User(1L, "testName", "testLogin", "user@mail.ru", LocalDate.of(2000, 12, 12));
        User user2 = new User(1L, "testName1", "testLogin1", "user1@mail.ru", LocalDate.of(2005, 12, 12));
        userController.createUser(user1);
        userController.createUser(user2);
        List<User> users = userController.getAllUsers();

        assertEquals(2, users.size(), "Пользователей в списке должно быть 2");
    }
}
