package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.yandex.practicum.filmorate.model.Review;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private void createReviewInDb() {
        jdbcTemplate.update("INSERT INTO film_reviews (film_id, user_id, content, is_positive, useful) VALUES (1, 1, 'Initial Review', true, 0)");
    }

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM reviews_likes");
        jdbcTemplate.execute("DELETE FROM film_reviews");
        jdbcTemplate.execute("DELETE FROM films");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("ALTER TABLE film_reviews ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE films ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("INSERT INTO users (name, login, email, birthday) VALUES ('Test User', 'testuser', 'test@example.com', '2000-01-01')");
        jdbcTemplate.update("INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES ('Test Film', 'Test Description', '2020-01-01', 120, 1)");
        createReviewInDb();
    }

    @Test
    void createReview() throws Exception {
        Review review = Review.builder()
                .content("This film is awesome!")
                .isPositive(true)
                .filmId(1L)
                .userId(1L)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("This film is awesome!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isPositive").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.filmId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1));
    }

    @Test
    void updateReview() throws Exception {
        Review review = Review.builder()
                .reviewId(1L)
                .content("Updated content")
                .isPositive(false)
                .filmId(1L)
                .userId(1L)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.put("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Updated content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isPositive").value(false));
    }

    @Test
    void getReviewById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviewId").value(1));
    }

    @Test
    void getReviewsByFilmId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reviews?filmId=1&count=5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].filmId").value(1));
    }

    @Test
    void likeReview() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/reviews/1/like/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviewId").value(1));
    }

    @Test
    void dislikeReview() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/reviews/1/dislike/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviewId").value(1));
    }

    @Test
    void deleteLikeReview() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/reviews/1/like/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviewId").value(1));
    }

    @Test
    void deleteDislikeReview() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/reviews/1/dislike/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviewId").value(1));
    }

    @Test
    void deleteReview() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/reviews/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
