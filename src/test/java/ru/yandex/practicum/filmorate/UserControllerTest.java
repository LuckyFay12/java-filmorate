package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    public static final String PATH = "/users";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update(
                "INSERT INTO users(name, login, email, birthday) " +
                        "VALUES ('Иван', 'Login1', 'ivan@mail.ru', '2000-11-11')");
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("controller/user/create/request/user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getContentFromFile("controller/user/create/response/user.json")
                ));
    }

    private String getContentFromFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(
                    Objects.requireNonNull(getClass().getClassLoader().getResource(path)).toURI()
            )));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка: " + path, e);
        }
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("controller/user/update/request/user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getContentFromFile("controller/user/update/response/user.json")
                ));
    }

    @Test
    void getById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getContentFromFile("controller/user/get_by_id/response/user.json")
                ));
    }
}
