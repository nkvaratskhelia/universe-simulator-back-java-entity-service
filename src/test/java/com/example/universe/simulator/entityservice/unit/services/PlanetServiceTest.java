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
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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

    private static Planet planet;
    private static UUID id;
    private static List<Planet> planets;

    @BeforeAll
    static void setUpContext() {
        id = UUID.randomUUID();
        planet = Planet.builder().id(id).name("earth").build();
        planets = List.of(planet);
    }

    @Test
    @DisplayName("test planetService.get success")
    void testGet() throws AppException {
        // arrange
        given(repo.findById(id)).willReturn(Optional.of(planet));
        // act
        Planet resultPlanet = service.get(id);
        // assert
        assertEquals(planet, resultPlanet);
        then(repo).should().findById(id);
    }

    @Test
    @DisplayName("test planetService.get fail")
    void testGetException() {
        // arrange
        given(repo.findById(id)).willReturn(Optional.empty());
        // act, assert
        AppException appException = assertThrows(AppException.class, () -> service.get(id));
        assertEquals(ErrorCodeType.ENTITY_NOT_FOUND, appException.getErrorCode());
        then(repo).should().findById(id);
    }

    @Test
    @DisplayName("test planetService.getList")
    void testGetList() {
        // arrange
        given(repo.findAll()).willReturn(planets);
        // act
        List<Planet> resultPlanets = service.getList();
        // assert
        assertEquals(planets, resultPlanets);
        then(repo).should().findAll();
    }

    @Test
    @DisplayName("test planetService.add")
    void testAdd() {
        // arrange
        given(repo.save(planet)).willReturn(planet);
        // act
        Planet resultPlanet = service.add(planet);
        // assert
        assertEquals(planet, resultPlanet);
        then(repo).should().save(planet);
    }

    @Test
    @DisplayName("test planetService.update success")
    void testUpdate() throws AppException {
        // arrange
        given(repo.findById(id)).willReturn(Optional.of(planet));
        given(repo.save(planet)).willReturn(planet);
        // act
        Planet resultPlanet = service.update(planet);
        // assert
        assertEquals(planet, resultPlanet);
        then(repo).should().findById(id);
        then(repo).should().save(planet);
    }

    @Test
    @DisplayName("test planetService.update fail")
    void testUpdateException() {
        // arrange
        given(repo.findById(id)).willReturn(Optional.empty());
        // act, assert
        AppException appException = assertThrows(AppException.class, () -> service.update(planet));
        assertEquals(ErrorCodeType.ENTITY_NOT_FOUND, appException.getErrorCode());
        then(repo).should().findById(id);
        then(repo).should(never()).save(planet);
    }

    @Test
    @DisplayName("test planetService.delete success")
    void testDelete() {
        // arrange
        given(repo.findById(id)).willReturn(Optional.of(planet));
        // act, assert
        assertDoesNotThrow(() -> service.delete(id));
        then(repo).should().findById(id);
        then(repo).should().deleteById(id);
    }

    @Test
    @DisplayName("test planetService.delete fail")
    void testDeleteException() throws AppException {
        // arrange
        given(repo.findById(id)).willReturn(Optional.empty());
        // act, assert
        AppException appException = assertThrows(AppException.class, () -> service.delete(id));
        assertEquals(ErrorCodeType.ENTITY_NOT_FOUND, appException.getErrorCode());
        then(repo).should().findById(id);
        then(repo).should(never()).deleteById(id);
    }

}
