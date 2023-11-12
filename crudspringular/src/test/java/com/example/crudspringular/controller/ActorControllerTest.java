package com.example.crudspringular.controller;

import com.example.crudspringular.dto.ActorDTO;
import com.example.crudspringular.entity.Actor;
import com.example.crudspringular.service.ActorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ActorControllerTest {

    @Mock
    private ActorService actorService;

    @InjectMocks
    private ActorController actorController;

    private MockMvc mockMvc;
    private ActorDTO actorDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(actorController).build();

        actorDTO = new ActorDTO();
        actorDTO.setId(1L);
        actorDTO.setName("John Doe");
        // Set other properties as needed
    }

    @Test
    void testCreateActor() throws Exception {
        when(actorService.saveActor(any(ActorDTO.class))).thenReturn(actorDTO);

        mockMvc.perform(post("/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\"}")) // Adjust JSON to match ActorDTO structure
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(actorDTO.getName()));

        verify(actorService).saveActor(any(ActorDTO.class));
    }

    @Test
    void testGetAllActors() throws Exception {
        List<ActorDTO> actorDTOs = Arrays.asList(actorDTO);
        when(actorService.findAllActors()).thenReturn(actorDTOs);

        mockMvc.perform(get("/actors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(actorDTO.getName()));

        verify(actorService).findAllActors();
    }

    @Test
    void testGetActorById() throws Exception {
        when(actorService.findActorById(1L)).thenReturn(new Actor());
        when(actorService.convertToDto(any(Actor.class))).thenReturn(actorDTO);

        mockMvc.perform(get("/actors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(actorDTO.getName()));

        verify(actorService).findActorById(1L);
    }

    @Test
    void testSearchActors() throws Exception {
        Page<ActorDTO> page = new PageImpl<>(Arrays.asList(actorDTO));
        when(actorService.searchActors(anyString(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/actors/search?query=John&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(actorDTO.getName()));

        verify(actorService).searchActors(anyString(), anyInt(), anyInt());
    }

    @Test
    void testDeleteActor() throws Exception {
        mockMvc.perform(delete("/actors/1"))
                .andExpect(status().isNoContent());

        verify(actorService).deleteActor(1L);
    }
}
