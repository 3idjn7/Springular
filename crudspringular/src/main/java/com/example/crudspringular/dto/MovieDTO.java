package com.example.crudspringular.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.util.List;

@Data
public class MovieDTO {
    private Long id;

    @NotBlank(message = "Title must not be blank")
    private String title;

    @Min(value = 1900, message = "Release year must be after 1900")
    @Max(value = 2100, message = "Release year must be before 2100")
    private Integer releaseYear;

    @Valid
    private List<GenreDTO> genres;

    @Valid
    private List<ActorDTO> actors;
}