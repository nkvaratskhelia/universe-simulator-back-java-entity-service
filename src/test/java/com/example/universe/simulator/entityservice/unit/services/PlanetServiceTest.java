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
    @DisplayName("test planetService.get success")
    void testGet() throws AppException {
        // arrange
        given(repo.findById(id)).willReturn(Optional.of(planet));
        // act
        Planet resultPlanet = service.get(id);
        // assert
        assertThat(resultPlanet).isEqualTo(planet);
        then(repo).should().findById(id);
    }

    @Test
    @DisplayName("test planetService.get fail")
    void testGet_entityNotFound() {
        // arrange
        given(repo.findById(id)).willReturn(Optional.empty());
        // act, assert
        AppException appException = catchThrowableOfType(() -> service.get(id), AppException.class);
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCodeType.ENTITY_NOT_FOUND);
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
        assertThat(resultPlanets).isEqualTo(planets);
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
        assertThat(resultPlanet).isEqualTo(planet);
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
        assertThat(resultPlanet).isEqualTo(planet);
        then(repo).should().findById(id);
        then(repo).should().save(planet);
    }

    @Test
    @DisplayName("test planetService.update fail")
    void testUpdate_entityNotFound() {
        // arrange
        given(repo.findById(id)).willReturn(Optional.empty());
        // act, assert
        AppException appException = catchThrowableOfType(() -> service.update(planet), AppException.class);
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCodeType.ENTITY_NOT_FOUND);
        then(repo).should().findById(id);
        then(repo).should(never()).save(planet);
    }

    @Test
    @DisplayName("test planetService.delete success")
    void testDelete() {
        // arrange
        given(repo.findById(id)).willReturn(Optional.of(planet));
        // act, assert
        assertThatCode(() -> service.delete(id)).doesNotThrowAnyException();
        then(repo).should().findById(id);
        then(repo).should().deleteById(id);
    }

    @Test
    @DisplayName("test planetService.delete fail")
    void testDelete_entityNotFound() {
        // arrange
        given(repo.findById(id)).willReturn(Optional.empty());
        // act, assert
        AppException appException = catchThrowableOfType(() -> service.delete(id), AppException.class);
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCodeType.ENTITY_NOT_FOUND);
        then(repo).should().findById(id);
        then(repo).should(never()).deleteById(id);
    }

}
