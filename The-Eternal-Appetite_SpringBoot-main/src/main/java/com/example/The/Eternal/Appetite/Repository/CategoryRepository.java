package com.example.The.Eternal.Appetite.Repository;

	import com.example.The.Eternal.Appetite.entity.Category;
	import org.springframework.data.jpa.repository.JpaRepository;

	public interface CategoryRepository extends JpaRepository<Category, Long> {
	    Category findByName(String name);
	}


