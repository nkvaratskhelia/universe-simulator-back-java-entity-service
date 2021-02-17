package com.example.universe.simulator.entityservice.unit.services;

import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.specifications.PlanetSpecification;
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
class PlanetServiceTest {

    @Mock
    private PlanetRepository repository;

    @Mock
    private StarRepository starRepository;

    @Mock
    private MoonRepository moonRepository;

    @InjectMocks
    private PlanetService service;

    @Test
    void testGetList() {
        //given
        List<Planet> list = List.of(
                Planet.builder().name("name").build()
        );
        Pageable pageable = Pageable.unpaged();
        Page<Planet> page = new PageImpl<>(list, pageable, list.size());
        Specification<Planet> specification = new PlanetSpecification().getSpecification(new PlanetFilter());

        given(repository.findAll(ArgumentMatchers.<Specification<Planet>>any(), any(Pageable.class)))
                .willReturn(page);
        //when
        Page<Planet> result = service.getList(specification, pageable);
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
        Planet entity = Planet.builder().name("name").build();
        given(repository.findById(any())).willReturn(Optional.of(entity));
        //when
        Planet result = service.get(id);
        //then
        assertThat(result).isEqualTo(entity);
        then(repository).should().findById(id);
    }

    @Test
    void testAdd_duplicateName() {
        //given
        Planet entity = Planet.builder().name("name").build();
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
    void testAdd_starNotFound() {
        //given
        Planet entity = Planet.builder()
                .name("name")
                .star(Star.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsByName(anyString())).willReturn(false);
        given(starRepository.existsById(any())).willReturn(false);
        //when
        AppException exception = catchThrowableOfType(() -> service.add(entity), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_STAR);
        then(starRepository).should().existsById(entity.getStar().getId());
        then(repository).should(never()).save(any());
    }

    @Test
    void testAdd_successfulAdd() throws AppException {
        //given
        Planet entity = Planet.builder()
                .name("name")
                .star(Star.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsByName(anyString())).willReturn(false);
        given(starRepository.existsById(any())).willReturn(true);
        given(repository.save(any())).willReturn(entity);
        //when
        Planet result = service.add(entity);
        //then
        assertThat(result).isEqualTo(entity);
        then(repository).should().save(entity);
    }

    @Test
    void testUpdate_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        Planet entity = Planet.builder().id(id).build();
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
        Planet entity = Planet.builder().id(id).name("name").build();
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
    void testUpdate_starNotFound() {
        //given
        UUID id = UUID.randomUUID();
        Planet entity = Planet.builder()
                .id(id)
                .name("name")
                .star(Star.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(false);
        given(starRepository.existsById(any())).willReturn(false);
        //when
        AppException exception = catchThrowableOfType(() -> service.update(entity), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_STAR);
        then(starRepository).should().existsById(entity.getStar().getId());
        then(repository).should(never()).save(any());
    }

    @Test
    void testUpdate_successfulUpdate() throws AppException {
        //given
        UUID id = UUID.randomUUID();
        Planet entity = Planet.builder()
                .id(id)
                .name("name")
                .star(Star.builder().id(UUID.randomUUID()).build())
                .build();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(false);
        given(starRepository.existsById(any())).willReturn(true);
        given(repository.save(any())).willReturn(entity);
        //when
        Planet result = service.update(entity);
        //then
        assertThat(result).isEqualTo(entity);
        then(repository).should().save(entity);
    }

    @Test
    void testDelete_inUse() {
        //given
        UUID id = UUID.randomUUID();
        given(moonRepository.existsByPlanetId(any())).willReturn(true);
        //when
        AppException exception = catchThrowableOfType(() -> service.delete(id), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.IN_USE);
        then(moonRepository).should().existsByPlanetId(id);
        then(repository).should(never()).deleteById(any());
    }

    @Test
    void testDelete_successfulDelete() throws AppException {
        //given
        UUID id = UUID.randomUUID();
        given(moonRepository.existsByPlanetId(any())).willReturn(false);
        //when
        service.delete(id);
        //then
        then(repository).should().deleteById(id);
    }
}
