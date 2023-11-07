package com.example.crudspringular.dto;

import lombok.Data;

import java.util.Set;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private Integer releaseYear;
    private GenreDTO genre;
    private Set<ActorDTO> actors;
}