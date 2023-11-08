package com.example.crudspringular.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private Integer releaseYear;
    private List<GenreDTO> genres;
    private List<ActorDTO> actors;
}