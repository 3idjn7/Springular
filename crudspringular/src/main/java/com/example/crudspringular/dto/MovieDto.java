package com.example.crudspringular.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class MovieDto {
    private String title;
    private Integer releaseYear;
    private Set<Long> genreIds;
    private Set<Long> actorIds;
}
