package com.example.crudspringular.controller;

import com.example.crudspringular.entity.Actor;
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
    public ResponseEntity<Actor> createActor(@RequestBody Actor actor) {
        log.info("Received request to create actor: {}", actor.getName());
        Actor savedActor = actorService.saveActor(actor);
        log.info("Created actor with ID: {}", savedActor.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedActor);
    }

    @GetMapping
    public ResponseEntity<List<Actor>> getAllActors() {
        log.info("Received a request to list all actors");
        List<Actor> actors = actorService.findAllActors();
        log.info("Returning {} actors", actors.size());
        return ResponseEntity.ok(actors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Long id) {
        log.info("Received request to get actor with ID: {}", id);
        Optional<Actor> actor = actorService.findActorById(id);
        if (actor.isPresent()) {
            log.info("Returning actor with ID: {}", id);
            return ResponseEntity.ok(actor.get());
        } else {
            log.info("Actor with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        log.info("Received request to delete actor with ID: {}", id);
        actorService.deleteActor(id);
        log.info("Deleted actor with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
