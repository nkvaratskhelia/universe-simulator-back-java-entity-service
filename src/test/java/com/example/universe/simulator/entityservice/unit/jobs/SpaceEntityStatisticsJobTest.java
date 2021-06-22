package com.example.universe.simulator.entityservice.unit.jobs;

import com.example.universe.simulator.entityservice.events.EventPublisher;
import com.example.universe.simulator.entityservice.jobs.SpaceEntityStatisticsJob;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import com.example.universe.simulator.entityservice.types.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SpaceEntityStatisticsJobTest {

    @Mock
    private GalaxyRepository galaxyRepository;

    @Mock
    private StarRepository starRepository;

    @Mock
    private PlanetRepository planetRepository;

    @Mock
    private MoonRepository moonRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private SpaceEntityStatisticsJob spaceEntityStatisticsJob;

    @Test
    void testLogStatistics() {
        // given
        long numGalaxies = 1L, numStars = 1L, numPlanets = 1L, numMoons = 1L;
        String statistics = String.format("galaxies [%d], stars [%d], planets [%d], moons [%d]", numGalaxies, numStars, numPlanets, numMoons);
        given(galaxyRepository.count()).willReturn(numGalaxies);
        given(starRepository.count()).willReturn(numStars);
        given(planetRepository.count()).willReturn(numPlanets);
        given(moonRepository.count()).willReturn(numMoons);
        // when
        spaceEntityStatisticsJob.logStatistics();
        // then
        then(galaxyRepository).should().count();
        then(starRepository).should().count();
        then(planetRepository).should().count();
        then(moonRepository).should().count();
        then(eventPublisher).should().publishEvent(EventType.SPACE_ENTITY_STATISTICS, statistics);
    }
}
