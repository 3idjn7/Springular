package com.example.crudspringular.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenreDTO {
    private Long id;

    @NotBlank(message = "Title must not be blank")
    private String name;
}