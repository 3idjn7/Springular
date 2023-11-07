package com.example.crudspringular.controller;

import com.example.crudspringular.dto.ActorDTO;
import com.example.crudspringular.service.ActorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/actors")
public class ActorController {
    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @PostMapping
    public ResponseEntity<ActorDTO> createActor(@RequestBody ActorDTO actorDTO) {
        log.info("Received request to create actor: {}", actorDTO.getName());
        ActorDTO savedActorDTO = actorService.saveActor(actorDTO);
        log.info("Created actor with ID: {}", savedActorDTO.getId());
        return new ResponseEntity<>(savedActorDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ActorDTO>> getAllActors() {
        log.info("Received a request to list all actors");
        List<ActorDTO> actorDTOs = actorService.findAllActors();
        log.info("Returning {} actors", actorDTOs.size());
        return ResponseEntity.ok(actorDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDTO> getActorById(@PathVariable Long id) {
        log.info("Received request to get actor with ID: {}", id);
        Optional<ActorDTO> actorDTO = actorService.findActorById(id);
        return actorDTO.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.info("Actor with ID: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        log.info("Received request to delete actor with ID: {}", id);
        actorService.deleteActor(id);
        log.info("Deleted actor with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}