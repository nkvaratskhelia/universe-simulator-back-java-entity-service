package com.example.universe.simulator.entityservice.jobs;

import com.example.universe.simulator.entityservice.services.GalaxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class SpaceEntityStatisticsJob {

    private final GalaxyService galaxyService;

    @Scheduled(cron = "${app.scheduling.space-entity-statistics-job-cron}")
    void createSpaceEntities() {

    }
}
