package com.user.steammgmt.repository;

import com.user.steammgmt.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, String> {
	// String là kiểu dữ liệu của khóa chính publisherId
}
