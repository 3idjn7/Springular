package com.example.crudspringular.service;

import com.example.crudspringular.dto.ActorDTO;
import com.example.crudspringular.entity.Actor;
import com.example.crudspringular.repository.ActorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActorService {
    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public ActorDTO saveActor(ActorDTO actorDTO) {
        log.info("Attempting to save actor: {}", actorDTO.getName());
        Actor actor = convertToEntity(actorDTO);
        Actor savedActor = actorRepository.save(actor);
        ActorDTO savedActorDTO = convertToDto(savedActor);
        log.info("Saved actor: {}", savedActorDTO.getName());
        return savedActorDTO;
    }

    public Page<ActorDTO> searchActors(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return actorRepository.findByNameContainingIgnoreCase(name, pageRequest)
                .map(this::convertToDto);
    }

    public List<ActorDTO> findAllActors() {
        log.info("Attempting to fetch all actors");
        List<ActorDTO> actors = actorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        log.info("Fetched {} actors", actors.size());
        return actors;
    }

    public Actor findActorById(Long id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actor not found"));
    }

    public void deleteActor(Long id) {
        log.info("Attempting to delete actor with ID: {}", id);
        actorRepository.deleteById(id);
        log.info("Deleted actor with ID: {}", id);
    }

    public ActorDTO convertToDto(Actor actor) {
        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setId(actor.getId());
        actorDTO.setName(actor.getName());
        return actorDTO;
    }

    private Actor convertToEntity(ActorDTO actorDTO) {
        Actor actor = new Actor();
        actor.setId(actorDTO.getId());
        actor.setName(actorDTO.getName());
        // Map other fields as necessary
        return actor;
    }
}