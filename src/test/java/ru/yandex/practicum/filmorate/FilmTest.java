package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmTest {

    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    @DisplayName("FILM: Валидация на корректные данные")
    void validateCorrectAllData() {
        Film film = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.parse("1967-04-23"))
                .duration(112)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertEquals(0, violations.size(), "Violations found");
    }

    @Test
    @DisplayName("FILM: Валидация на пустое наименование")
    void validateNotCorrectName() {
        Film film = Film.builder()
                .id(1L)
                .name(" ")
                .description("description")
                .releaseDate(LocalDate.parse("1967-04-23"))
                .duration(112)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertEquals(1, violations.size(), "Violations not found");
    }

    @Test
    @DisplayName("FILM: Валидация длины описания (> 200 симв.)")
    void validateLongDescription() {
        Film film = Film.builder()
                .id(1L)
                .name("name")
                .description("descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription")
                .releaseDate(LocalDate.parse("1967-04-23"))
                .duration(112)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertEquals(1, violations.size(), "Violations not found");
    }

    @Test
    @DisplayName("FILM: Валидация на не корректную releaseDate")
    void validateNotNullReleaseDate() {
        Film film = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.parse("1867-04-23"))
                .duration(112)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertEquals(1, violations.size(), "Violations not found");
    }

    @Test
    @DisplayName("FILM: Валидация на положительную продолжительность ")
    void validatePositiveDuration() {
        Film film = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.parse("1967-04-23"))
                .duration(0)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertEquals(1, violations.size(), "Violations not found");
    }

}