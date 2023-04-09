package com.github.m2cyurealestate.real_estate_back.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The base interface that all daos must implement (instead of only JpaRepository for example).
 * It will permit adding functionalities if needed
 * @author Aldric Vitali Silvestre
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
}
