package com.example.crudspringular.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ActorDTO {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
}