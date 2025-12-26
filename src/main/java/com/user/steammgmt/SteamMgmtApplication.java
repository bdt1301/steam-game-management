package com.user.steammgmt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.user.steammgmt.model.User;
import com.user.steammgmt.repository.UserRepository;

@SpringBootApplication
public class SteamMgmtApplication {
	public static void main(String[] args) {
		SpringApplication.run(SteamMgmtApplication.class, args);
	}

	@Bean
	CommandLineRunner initUsers(UserRepository repo, PasswordEncoder encoder) {
		return args -> {
			if (repo.count() == 0) {
				repo.save(new User("admin", encoder.encode("admin123"), "ROLE_ADMIN", "Admin", "admin@gmail.com",
						"/images/default_avatar_admin.jpg"));
			}
		};
	}
}
