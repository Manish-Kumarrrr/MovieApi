package com.movieflix.movieAPI.repositories;

import com.movieflix.movieAPI.entities.Movie;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Integer> {

}
