package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @PostMapping
    public Director add(@Valid @RequestBody Director director) {
        log.info("Получен HTTP-запрос на добавление режиссёра {}", director);
        Director createdDirector = directorService.add(director);
        log.info("Успешно обработан HTTP-запрос на добавление фильма {}", director);
        return createdDirector;
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        log.info("Получен HTTP-запрос на обновление режиссёра с id {}", director.getId());
        Director updatedDirector = directorService.update(director);
        log.info("Успешно обработан HTTP-запрос на обновление режиссёра с id {}", director.getId());
        return updatedDirector;
    }

    @GetMapping
    public List<Director> getAll() {
        log.info("Получен HTTP-запрос на список жанров");
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public Director getById(@PathVariable Long id) {
        log.info("Получен запрос на получение режиссёра по id {}", id);
        return directorService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Получен запрос на удаление режиссёра по id {}", id);
        directorService.deleteById(id);
    }
}
