package com.example.universe.simulator.entityservice.unit.services;

import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import com.example.universe.simulator.entityservice.services.PlanetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class PlanetServiceTest {

    @Mock
    private PlanetRepository repository;

    @Mock
    private StarRepository starRepository;

    @InjectMocks
    private PlanetService service;

    @Test
    void testGetList() {
        // given
        List<Planet> list = List.of(Planet.builder().name("name").build());
        given(repository.findAll()).willReturn(list);
        // when
        List<Planet> result = service.getList();
        // then
        assertThat(result).isEqualTo(list);
        then(repository).should().findAll();
    }

    @Test
    void testGet() throws AppException {
        // given
        UUID id = UUID.randomUUID();
        Planet entity = Planet.builder().name("name").build();
        given(repository.findById(any())).willReturn(Optional.of(entity));
        // when
        Planet result = service.get(id);
        // then
        assertThat(result).isEqualTo(entity);
        then(repository).should().findById(id);
    }

    @Test
    void testGet_planetNotFound() {
        // given
        UUID id = UUID.randomUUID();
        given(repository.findById(any())).willReturn(Optional.empty());
        // when, then
        AppException appException = catchThrowableOfType(() -> service.get(id), AppException.class);
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_ENTITY);
        then(repository).should().findById(id);
    }

    @Test
    void testAdd() throws AppException {
        // given
        Planet entity = Planet
                .builder()
                .name("name")
                .star(Star.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsByName(anyString())).willReturn(false);
        given(starRepository.existsById(any())).willReturn(true);
        given(repository.save(any())).willReturn(entity);
        // when
        Planet result = service.add(entity);
        // then
        assertThat(result).isEqualTo(entity);
        then(repository).should().existsByName(entity.getName());
        then(starRepository).should().existsById(entity.getStar().getId());
        then(repository).should().save(entity);
    }

    @Test
    void testAdd_duplicateName() {
        // given
        Planet entity = Planet.builder().name("name").build();
        given(repository.existsByName(anyString())).willReturn(true);
        // when, then
        AppException appException = catchThrowableOfType(() -> service.add(entity), AppException.class);
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCodeType.EXISTS_NAME);
        then(repository).should().existsByName(entity.getName());
        then(starRepository).should(never()).existsById(any());
        then(repository).should(never()).save(any());
    }

    @Test
    void testAdd_starNotFound() {
        // given
        Planet entity = Planet
                .builder()
                .name("name")
                .star(Star.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsByName(anyString())).willReturn(false);
        given(starRepository.existsById(any())).willReturn(false);
        // when, then
        AppException appException = catchThrowableOfType(() -> service.add(entity), AppException.class);
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_STAR);
        then(repository).should().existsByName(entity.getName());
        then(starRepository).should().existsById(entity.getStar().getId());
        then(repository).should(never()).save(any());
    }

    @Test
    void testUpdate() throws AppException {
        // given
        Planet entity = Planet
                .builder()
                .name("name")
                .star(Star.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(false);
        given(starRepository.existsById(any())).willReturn(true);
        given(repository.save(any())).willReturn(entity);
        // when
        Planet result = service.update(entity);
        // then
        assertThat(result).isEqualTo(entity);
        then(repository).should().existsById(entity.getId());
        then(repository).should().existsByNameAndIdNot(entity.getName(), entity.getId());
        then(starRepository).should().existsById(entity.getStar().getId());
        then(repository).should().save(entity);
    }

    @Test
    void testUpdate_planetNotFound() {
        // given
        Planet entity = Planet
                .builder()
                .name("name")
                .build();
        given(repository.existsById(any())).willReturn(false);
        // when
        AppException appException = catchThrowableOfType(() -> service.update(entity), AppException.class);
        // then
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_ENTITY);
        then(repository).should().existsById(entity.getId());
        then(repository).should(never()).existsByNameAndIdNot(anyString(), any());
        then(starRepository).should(never()).existsById(any());
        then(repository).should(never()).save(any());
    }

    @Test
    void testUpdate_duplicateName() {
        // given
        Planet entity = Planet
                .builder()
                .name("name")
                .build();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(true);
        // when
        AppException appException = catchThrowableOfType(() -> service.update(entity), AppException.class);
        // then
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCodeType.EXISTS_NAME);
        then(repository).should().existsById(entity.getId());
        then(repository).should().existsByNameAndIdNot(entity.getName(), entity.getId());
        then(starRepository).should(never()).existsById(any());
        then(repository).should(never()).save(any());
    }

    @Test
    void testUpdate_starNotFound() {
        // given
        Planet entity = Planet
                .builder()
                .name("name")
                .star(Star.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(false);
        given(starRepository.existsById(any())).willReturn(false);
        // when
        AppException appException = catchThrowableOfType(() -> service.update(entity), AppException.class);
        // then
        assertThat(appException.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_STAR);
        then(repository).should().existsById(entity.getId());
        then(repository).should().existsByNameAndIdNot(entity.getName(), entity.getId());
        then(starRepository).should().existsById(entity.getStar().getId());
        then(repository).should(never()).save(any());
    }

    @Test
    void testDelete() {
        // given
        UUID id = UUID.randomUUID();
        // when
        service.delete(id);
        // then
        then(repository).should().deleteById(id);
    }

}
