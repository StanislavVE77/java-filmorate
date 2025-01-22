package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
@DisplayName("UserDbStorage")

class FilmorateApplicationTests {
	public static final long TEST_USER_ID = 1L;
	private final UserDbStorage userDbStorage;


	static User getTestUser() {
		User user = new User();
		user.setId(TEST_USER_ID);
		user.setLogin("login");
		user.setEmail("user@mail.ru");
		user.setName("Name");
		user.setBirthday(LocalDate.of(2000, 3, 22));
		return user;
	}


	@Test
	@DisplayName("Проверка данных пользователя")
	public void shouldFindUserById() {
		User testUser = getTestUser();
		Optional<User> userOptional = userDbStorage.findUserById(1);

		assertThat(userOptional)
				.isPresent()
				.get()
				.usingRecursiveComparison()
				.isEqualTo(testUser);
	}
}