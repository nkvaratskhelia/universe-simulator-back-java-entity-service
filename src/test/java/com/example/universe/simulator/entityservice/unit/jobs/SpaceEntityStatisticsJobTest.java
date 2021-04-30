package com.example.universe.simulator.entityservice.unit.jobs;

import com.example.universe.simulator.entityservice.jobs.SpaceEntityStatisticsJob;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private SpaceEntityStatisticsJob spaceEntityStatisticsJob;

    @Test
    void testLogStatistics() {
        //when
        spaceEntityStatisticsJob.logStatistics();
        //then
        then(galaxyRepository).should().count();
        then(starRepository).should().count();
        then(planetRepository).should().count();
        then(moonRepository).should().count();
    }
}
