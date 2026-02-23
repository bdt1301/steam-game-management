package com.user.steammgmt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	@Column(nullable = false)
	private String categoryName;

	@Column(length = 1000)
	private String categoryDescription;

	@Column(length = 1000)
	private String categoryImage;

	@Transient
	private Game featuredGame;

	@Transient
	private int productQuantity;

	@ManyToMany(mappedBy = "categories")
	private List<Game> games;

	public Category() {
	}

	public Category(Long categoryId, String categoryName, String categoryDescription, String categoryImage) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryDescription = categoryDescription;
		this.categoryImage = categoryImage;
	}

}