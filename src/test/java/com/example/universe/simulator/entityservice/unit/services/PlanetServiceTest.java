package com.example.universe.simulator.entityservice.unit.services;

import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.services.PlanetService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class PlanetServiceTest {

    @Mock
    private PlanetRepository repo;

    @InjectMocks
    private PlanetService service;

    private static UUID id;
    private static Planet planet;
    private static List<Planet> planets;

    @BeforeAll
    static void setUpContext() {
        id = UUID.randomUUID();
        planet = Planet.builder().id(id).name("name").version(0L).build();
        planets = List.of(planet);
    }

    @Test
    void testGet() throws AppException {
        // given
        given(repo.findById(any())).willReturn(Optional.of(planet));
        // when
        Planet resultPlanet = service.get(id);
        // then
        assertThat(resultPlanet).isEqualTo(planet);
        then(repo).should().findById(id);
    }

    @Test
    void testGet_entityNotFound() {
        // given
        given(repo.findById(any())).willReturn(Optional.empty());
        // when, then
        AppException appException = catchThrowableOfType(() -> service.get(id), AppException.class);
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCodeType.ENTITY_NOT_FOUND);
        then(repo).should().findById(id);
    }

    @Test
    void testGetList() {
        // given
        given(repo.findAll()).willReturn(planets);
        // when
        List<Planet> resultPlanets = service.getList();
        // then
        assertThat(resultPlanets).isEqualTo(planets);
        then(repo).should().findAll();
    }

    @Test
    void testAdd() {
        // given
        given(repo.save(any())).willReturn(planet);
        // when
        Planet resultPlanet = service.add(planet);
        // then
        assertThat(resultPlanet).isEqualTo(planet);
        then(repo).should().save(planet);
    }

    @Test
    void testUpdate() throws AppException {
        // given
        given(repo.findById(any())).willReturn(Optional.of(planet));
        given(repo.save(any())).willReturn(planet);
        // when
        Planet resultPlanet = service.update(planet);
        // then
        assertThat(resultPlanet).isEqualTo(planet);
        then(repo).should().findById(id);
        then(repo).should().save(planet);
    }

    @Test
    void testUpdate_entityNotFound() {
        // given
        given(repo.findById(any())).willReturn(Optional.empty());
        // when, then
        AppException appException = catchThrowableOfType(() -> service.update(planet), AppException.class);
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCodeType.ENTITY_NOT_FOUND);
        then(repo).should().findById(id);
        then(repo).should(never()).save(any());
    }

    @Test
    void testDelete() {
        // when
        service.delete(id);
        // then
        then(repo).should().deleteById(id);
    }

}
