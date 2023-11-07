package com.example.crudspringular.service;

import com.example.crudspringular.dto.ActorDTO;
import com.example.crudspringular.entity.Actor;
import com.example.crudspringular.repository.ActorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public List<ActorDTO> findAllActors() {
        log.info("Attempting to fetch all actors");
        List<ActorDTO> actors = actorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        log.info("Fetched {} actors", actors.size());
        return actors;
    }

    public Optional<ActorDTO> findActorById(Long id) {
        log.info("Attempting to find actor with ID: {}", id);
        Optional<ActorDTO> actorDTO = actorRepository.findById(id)
                .map(this::convertToDto);
        log.info("Found actor with ID {}: {}", id, actorDTO.isPresent() ? "present" : "not present");
        return actorDTO;
    }

    public void deleteActor(Long id) {
        log.info("Attempting to delete actor with ID: {}", id);
        actorRepository.deleteById(id);
        log.info("Deleted actor with ID: {}", id);
    }

    private ActorDTO convertToDto(Actor actor) {
        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setId(actor.getId());
        actorDTO.setName(actor.getName());
        actorDTO.setDateOfBirth(actor.getDateOfBirth());
        // Map other fields as necessary
        return actorDTO;
    }

    private Actor convertToEntity(ActorDTO actorDTO) {
        Actor actor = new Actor();
        actor.setId(actorDTO.getId());
        actor.setName(actorDTO.getName());
        actor.setDateOfBirth(actorDTO.getDateOfBirth());
        // Map other fields as necessary
        return actor;
    }
}
