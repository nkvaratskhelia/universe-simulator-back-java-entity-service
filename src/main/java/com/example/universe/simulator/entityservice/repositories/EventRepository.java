package com.example.universe.simulator.entityservice.repositories;

import com.example.universe.simulator.entityservice.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {}
