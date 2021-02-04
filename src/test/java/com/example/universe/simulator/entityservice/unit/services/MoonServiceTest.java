package com.example.universe.simulator.entityservice.unit.services;

import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.services.MoonService;
import com.example.universe.simulator.entityservice.specifications.MoonSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
class MoonServiceTest {

    @Mock
    private MoonRepository repository;

    @Mock
    private PlanetRepository planetRepository;

    @InjectMocks
    private MoonService service;

    @Test
    void testGetList() {
        //given
        List<Moon> list = List.of(
                Moon.builder().name("name").build()
        );
        Pageable pageable = Pageable.unpaged();
        Page<Moon> page = new PageImpl<>(list, pageable, list.size());
        Specification<Moon> specification = new MoonSpecification().getSpecification(new MoonFilter());

        given(repository.findAll(ArgumentMatchers.<Specification<Moon>>any(), any(Pageable.class)))
                .willReturn(page);
        //when
        Page<Moon> result = service.getList(specification, pageable);
        //then
        assertThat(result).isEqualTo(page);
        then(repository).should().findAll(specification, pageable);
    }

    @Test
    void testGet_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        given(repository.findById(any())).willReturn(Optional.empty());
        //when
        AppException exception = catchThrowableOfType(() -> service.get(id), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_ENTITY);
        then(repository).should().findById(id);
    }

    @Test
    void testGet_successfulGet() throws AppException {
        //given
        UUID id = UUID.randomUUID();
        Moon entity = Moon.builder().name("name").build();
        given(repository.findById(any())).willReturn(Optional.of(entity));
        //when
        Moon result = service.get(id);
        //then
        assertThat(result).isEqualTo(entity);
        then(repository).should().findById(id);
    }

    @Test
    void testAdd_duplicateName() {
        //given
        Moon entity = Moon.builder().name("name").build();
        given(repository.existsByName(anyString())).willReturn(true);
        //when
        AppException exception = catchThrowableOfType(() -> service.add(entity), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.EXISTS_NAME);
        then(repository).should().existsByName(entity.getName());
        then(repository).should(never()).existsByNameAndIdNot(anyString(), any());
        then(repository).should(never()).save(any());
    }

    @Test
    void testAdd_planetNotFound() {
        //given
        Moon entity = Moon.builder()
                .name("name")
                .planet(Planet.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsByName(anyString())).willReturn(false);
        given(planetRepository.existsById(any())).willReturn(false);
        //when
        AppException exception = catchThrowableOfType(() -> service.add(entity), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_PLANET);
        then(planetRepository).should().existsById(entity.getPlanet().getId());
        then(repository).should(never()).save(any());
    }

    @Test
    void testAdd_successfulAdd() throws AppException {
        //given
        Moon entity = Moon.builder()
                .name("name")
                .planet(Planet.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsByName(anyString())).willReturn(false);
        given(planetRepository.existsById(any())).willReturn(true);
        given(repository.save(any())).willReturn(entity);
        //when
        Moon result = service.add(entity);
        //then
        assertThat(result).isEqualTo(entity);
        then(repository).should().save(entity);
    }

    @Test
    void testUpdate_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        Moon entity = Moon.builder().id(id).build();
        given(repository.existsById(any())).willReturn(false);
        //when
        AppException exception = catchThrowableOfType(() -> service.update(entity), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_ENTITY);
        then(repository).should().existsById(id);
        then(repository).should(never()).save(any());
    }

    @Test
    void testUpdate_duplicateName() {
        //given
        UUID id = UUID.randomUUID();
        Moon entity = Moon.builder().id(id).name("name").build();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(true);
        //when
        AppException exception = catchThrowableOfType(() -> service.update(entity), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.EXISTS_NAME);
        then(repository).should().existsByNameAndIdNot(entity.getName(), id);
        then(repository).should(never()).existsByName(any());
        then(repository).should(never()).save(any());
    }

    @Test
    void testUpdate_planetNotFound() {
        //given
        UUID id = UUID.randomUUID();
        Moon entity = Moon.builder()
                .id(id)
                .name("name")
                .planet(Planet.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(false);
        given(planetRepository.existsById(any())).willReturn(false);
        //when
        AppException exception = catchThrowableOfType(() -> service.update(entity), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_PLANET);
        then(planetRepository).should().existsById(entity.getPlanet().getId());
        then(repository).should(never()).save(any());
    }

    @Test
    void testUpdate_successfulUpdate() throws AppException {
        //given
        UUID id = UUID.randomUUID();
        Moon entity = Moon.builder()
                .id(id)
                .name("name")
                .planet(Planet.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(false);
        given(planetRepository.existsById(any())).willReturn(true);
        given(repository.save(any())).willReturn(entity);
        //when
        Moon result = service.update(entity);
        //then
        assertThat(result).isEqualTo(entity);
        then(repository).should().save(entity);
    }

    @Test
    void testDelete_successfulDelete() {
        //given
        UUID id = UUID.randomUUID();
        //when
        service.delete(id);
        //then
        then(repository).should().deleteById(id);
    }
}
