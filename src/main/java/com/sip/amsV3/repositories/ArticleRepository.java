package com.sip.amsV3.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sip.amsV3.entities.Article;
@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
// on peut ajouter des requettes en langage SQL en utilisant 
	// @Query("");
	// public void add();
	
}
