package com.example.crudspringular.service;

import com.example.crudspringular.entity.Actor;
import com.example.crudspringular.repository.ActorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ActorService {
    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Transactional
    public Actor saveActor(Actor actor) {
        log.info("Attempting to save actor: {}", actor.getName());
        Actor savedActor = actorRepository.save(actor);
        log.info("Saved actor: {}", savedActor.getName());
        return savedActor;
    }

    public List<Actor> findAllActors() {
        log.info("Attempting to fetch all actors");
        List<Actor> actors = actorRepository.findAll();
        log.info("Fetched {} actors", actors.size());
        return actors;
    }

    public Optional<Actor> findActorById(Long id) {
        log.info("Attempting to find actor with ID: {}", id);
        Optional<Actor> actor = actorRepository.findById(id);
        log.info("Found actor with ID {}: {}", id, actor.isPresent() ? "present" : "not present");
        return actor;
    }

    @Transactional
    public Actor findOrCreateActor(String name) {
        log.info("Attempting to find or create actor with name: {}", name);
        return actorRepository.findByName(name)
                .orElseGet(() -> {
                    log.info("Actor not found, creating new actor with name: {}", name);
                    Actor newActor = new Actor();
                    newActor.setName(name);
                    return actorRepository.save(newActor);
                });
    }

    @Transactional
    public void deleteActor(Long id) {
        log.info("Attempting to delete actor with ID: {}", id);
        actorRepository.deleteById(id);
        log.info("Deleted actor with ID: {}", id);
    }
}
