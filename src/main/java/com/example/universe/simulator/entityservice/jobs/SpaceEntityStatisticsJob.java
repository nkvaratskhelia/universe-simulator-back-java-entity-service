package com.example.universe.simulator.entityservice.jobs;

import com.example.universe.simulator.entityservice.events.EventPublisher;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import com.example.universe.simulator.entityservice.types.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpaceEntityStatisticsJob {

    private final GalaxyRepository galaxyRepository;
    private final StarRepository starRepository;
    private final PlanetRepository planetRepository;
    private final MoonRepository moonRepository;
    private final EventPublisher eventPublisher;

    @Scheduled(cron = "${app.scheduling.space-entity-statistics-job-cron}")
    public void getStatistics() {
        log.info("getting space entity statistics...");
        long numGalaxies = galaxyRepository.count();
        long numStars = starRepository.count();
        long numPlanets = planetRepository.count();
        long numMoons = moonRepository.count();

        String statistics = String.format("galaxies [%d], stars [%d], planets [%d], moons [%d]", numGalaxies, numStars, numPlanets, numMoons);
        log.info("space entity statistics: {}", statistics);

        eventPublisher.publish(EventType.SPACE_ENTITY_STATISTICS, statistics);
    }
}
