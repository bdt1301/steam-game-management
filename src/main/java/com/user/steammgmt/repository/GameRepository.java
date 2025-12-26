package com.user.steammgmt.repository;

import com.user.steammgmt.model.Game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
	List<Game> findByPublisherPublisherId(String publisherId);

	List<Game> findByCategories_CategoryId(Long categoryId);
}