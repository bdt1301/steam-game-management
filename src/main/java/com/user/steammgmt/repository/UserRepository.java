package com.user.steammgmt.repository;

import com.user.steammgmt.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByEmailAndUsernameNot(String email, String username);
}
