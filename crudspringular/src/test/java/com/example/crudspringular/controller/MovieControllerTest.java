package com.example.crudspringular.controller;

import com.example.crudspringular.dto.MovieDTO;
import com.example.crudspringular.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;
    private MovieDTO movieDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();

        movieDTO = new MovieDTO();
        movieDTO.setId(1L);
        movieDTO.setTitle("Test Movie");
        // Set other properties as needed
    }

    @Test
    void testCreateMovie() throws Exception {
        when(movieService.saveMovie(any(MovieDTO.class))).thenReturn(movieDTO);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Movie\"}")) // Adjust JSON to match MovieDTO structure
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(movieDTO.getTitle()));

        verify(movieService).saveMovie(any(MovieDTO.class));
    }

    @Test
    void testUpdateMovie() throws Exception {
        when(movieService.updateMovie(eq(1L), any(MovieDTO.class))).thenReturn(movieDTO);

        mockMvc.perform(put("/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Test Movie\"}")) // Adjust JSON to match MovieDTO structure
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movieDTO.getTitle()));

        verify(movieService).updateMovie(eq(1L), any(MovieDTO.class));
    }

    @Test
    void testGetAllMovies() throws Exception {
        Page<MovieDTO> page = new PageImpl<>(Arrays.asList(movieDTO));
        when(movieService.findAllMovies(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/movies?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value(movieDTO.getTitle()));

        verify(movieService).findAllMovies(any(PageRequest.class));
    }

    @Test
    void testGetMovieById() throws Exception {
        when(movieService.findMovieById(1L)).thenReturn(Optional.of(movieDTO));

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movieDTO.getTitle()));

        verify(movieService).findMovieById(1L);
    }

    @Test
    void testSearchMovies() throws Exception {
        Page<MovieDTO> page = new PageImpl<>(Arrays.asList(movieDTO));
        when(movieService.searchMovies(anyString(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/movies/search?query=Test&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value(movieDTO.getTitle()));

        verify(movieService).searchMovies(anyString(), anyInt(), anyInt());
    }

    @Test
    void testDeleteMovie() throws Exception {
        mockMvc.perform(delete("/movies/1"))
                .andExpect(status().isNoContent());

        verify(movieService).deleteMovie(1L);
    }
}
