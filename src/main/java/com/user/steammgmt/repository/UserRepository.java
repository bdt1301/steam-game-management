package com.user.steammgmt.repository;

import com.user.steammgmt.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByRole(String role);

    List<User> findByFavoriteGames_AppIdIn(List<Long> gameIds);

    boolean existsByEmail(String email);

    boolean existsByEmailAndUsernameNot(String email, String username);
}
