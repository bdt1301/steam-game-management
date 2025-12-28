package com.user.steammgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.user.steammgmt.service.CustomUserDetailsService;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {

		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
				.requestMatchers("/", "/health", "/login", "/register", "/games", "/publishers", "/categories",
						"/{type}/details/{id}")
				.permitAll()
				.requestMatchers("/{type}/new", "/{type}/edit/{id}", "/{type}/delete/{id}", "/users", "records")
				.hasRole("ADMIN").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).failureUrl("/login?error")
						.permitAll())
				.logout(logout -> logout.logoutSuccessUrl("/"));
		return http.build();
	}

}
