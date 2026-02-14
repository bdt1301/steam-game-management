package com.user.steammgmt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.user.steammgmt.model.User;
import com.user.steammgmt.repository.UserRepository;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class SteamMgmtApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(SteamMgmtApplication.class, args);
    }

    @Bean
    CommandLineRunner initUsers(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new User("admin", encoder.encode("admin123"), "ROLE_ADMIN", "Admin", "admin@example.com",
                        "/images/default_avatar_admin.jpg"));
                repo.save(new User("user", encoder.encode("user123"), "ROLE_USER", "User", "user@example.com",
                        "/images/default_avatar_user.jpg"));
            }
        };
    }
}
