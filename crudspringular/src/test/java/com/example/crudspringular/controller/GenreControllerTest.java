package com.example.crudspringular.controller;

import com.example.crudspringular.dto.GenreDTO;
import com.example.crudspringular.entity.Genre;
import com.example.crudspringular.service.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class GenreControllerTest {

    @Mock
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;

    private MockMvc mockMvc;
    private GenreDTO genreDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();

        genreDTO = new GenreDTO();
        genreDTO.setId(1L);
        genreDTO.setName("Action");
        // Set other properties as needed
    }

    @Test
    void testCreateGenre() throws Exception {
        when(genreService.saveGenre(any(GenreDTO.class))).thenReturn(genreDTO);

        mockMvc.perform(post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Action\"}")) // Adjust JSON to match GenreDTO structure
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(genreDTO.getName()));

        verify(genreService).saveGenre(any(GenreDTO.class));
    }

    @Test
    void testGetAllGenres() throws Exception {
        List<GenreDTO> genreDTOs = Arrays.asList(genreDTO);
        when(genreService.findAllGenres()).thenReturn(genreDTOs);

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(genreDTO.getName()));

        verify(genreService).findAllGenres();
    }

    @Test
    void testGetGenreById() throws Exception {
        when(genreService.findGenreById(1L)).thenReturn(new Genre());
        when(genreService.convertToDto(any(Genre.class))).thenReturn(genreDTO);

        mockMvc.perform(get("/genres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(genreDTO.getName()));

        verify(genreService).findGenreById(1L);
    }

    @Test
    void testDeleteGenre() throws Exception {
        mockMvc.perform(delete("/genres/1"))
                .andExpect(status().isNoContent());

        verify(genreService).deleteGenre(1L);
    }
}
