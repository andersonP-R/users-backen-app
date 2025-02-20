package com.anderson.users;

import com.anderson.users.models.User;
import com.anderson.users.repositories.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UsersApplication {

	private final PasswordEncoder passwordEncoder;

    public UsersApplication(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

	//@Bean
	public CommandLineRunner commandLineRunner(
			UserRepository userRepository
	) {
		return args -> {
			for (int i = 0; i < 50; i++) {
				Faker faker = new Faker();
				var user = User.builder()
						.username(faker.name().username())
						.password(passwordEncoder.encode(faker.internet().password()))
						.email(faker.internet().emailAddress())
						.build();
				userRepository.save(user);
			}
		};
	}
}