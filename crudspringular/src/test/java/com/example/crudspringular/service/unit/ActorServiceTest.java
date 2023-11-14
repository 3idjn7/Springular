package com.example.crudspringular.service.unit;

import com.example.crudspringular.dto.ActorDTO;
import com.example.crudspringular.entity.Actor;
import com.example.crudspringular.repository.ActorRepository;
import com.example.crudspringular.service.ActorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @InjectMocks
    private ActorService actorService;

    private ActorDTO actorDTO;
    private Actor actor;

    @BeforeEach
    void setUp() {
        actorDTO = new ActorDTO();
        actorDTO.setId(1L);
        actorDTO.setName("John Doe");

        actor = new Actor();
        actor.setId(actorDTO.getId());
        actor.setName(actorDTO.getName());
    }

    @Test
    void testSaveActor() {
        when(actorRepository.save(any(Actor.class))).thenReturn(actor);

        ActorDTO savedActorDTO = actorService.saveActor(actorDTO);

        assertNotNull(savedActorDTO);
        assertEquals(actor.getName(), savedActorDTO.getName());
        verify(actorRepository).save(any(Actor.class));
    }

    @Test
    void testSearchActors() {
        String name = "John";
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Actor> page = new PageImpl<>(Arrays.asList(actor));
        when(actorRepository.findByNameContainingIgnoreCase(name, pageRequest)).thenReturn(page);

        Page<ActorDTO> result = actorService.searchActors(name, 0, 10);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(actorRepository).findByNameContainingIgnoreCase(name, pageRequest);
    }

    @Test
    void testFindAllActors() {
        when(actorRepository.findAll()).thenReturn(Arrays.asList(actor));

        List<ActorDTO> actors = actorService.findAllActors();

        assertNotNull(actors);
        assertFalse(actors.isEmpty());
        assertEquals(1, actors.size());
        verify(actorRepository).findAll();
    }

    @Test
    void testFindActorById() {
        Long actorId = 1L;
        when(actorRepository.findById(actorId)).thenReturn(Optional.of(actor));

        Actor foundActor = actorService.findActorById(actorId);

        assertNotNull(foundActor);
        assertEquals(actor.getId(), foundActor.getId());
        verify(actorRepository).findById(actorId);
    }

    @Test
    void testDeleteActor() {
        Long actorId = 1L;
        actorService.deleteActor(actorId);

        verify(actorRepository).deleteById(actorId);
    }
}
