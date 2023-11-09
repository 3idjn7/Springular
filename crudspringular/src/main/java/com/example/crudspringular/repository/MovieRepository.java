package com.example.crudspringular.repository;

import com.example.crudspringular.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m JOIN m.actors a WHERE " +
            "LOWER(m.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Movie> findByTitleContainingIgnoreCaseOrActorNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
}