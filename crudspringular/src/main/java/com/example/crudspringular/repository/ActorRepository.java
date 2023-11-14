package com.example.crudspringular.repository;

import com.example.crudspringular.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Actor> findByName(String name);
}