package com.example.universe.simulator.entityservice.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.events.EventPublisher;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.repositories.MoonRepository;
import com.example.universe.simulator.entityservice.repositories.PlanetRepository;
import com.example.universe.simulator.entityservice.services.MoonService;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import com.example.universe.simulator.entityservice.types.EventType;
import org.jspecify.annotations.NonNull;
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

@ExtendWith(MockitoExtension.class)
class MoonServiceTest {

    @Mock
    private MoonRepository repository;

    @Mock
    private PlanetRepository planetRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private MoonService service;

    @Test
    void testGetList() {
        // given
        List<Moon> list = List.of(
            TestUtils.buildMoon()
        );
        Pageable pageable = Pageable.unpaged();
        Page<@NonNull Moon> page = new PageImpl<>(list, pageable, list.size());

        given(repository.findAll(ArgumentMatchers.<Specification<@NonNull Moon>>any(), any(Pageable.class)))
            .willReturn(page);
        // when
        Page<@NonNull Moon> result = service.getList(Specification.unrestricted(), pageable);
        // then
        assertThat(result).isEqualTo(page);
        then(repository).should().findAll(Specification.unrestricted(), pageable);
    }

    @Test
    void testGet_idNotFound() {
        // given
        UUID id = UUID.randomUUID();
        given(repository.findById(any())).willReturn(Optional.empty());
        // when
        AppException exception = catchThrowableOfType(AppException.class, () -> service.get(id));
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_ENTITY);
        then(repository).should().findById(id);
    }

    @Test
    void testGet_successfulGet() throws AppException {
        // given
        UUID id = UUID.randomUUID();
        Moon entity = TestUtils.buildMoon();
        given(repository.findById(any())).willReturn(Optional.of(entity));
        // when
        Moon result = service.get(id);
        // then
        assertThat(result).isEqualTo(entity);
        then(repository).should().findById(id);
    }

    @Test
    void testAdd_duplicateName() {
        // given
        Moon entity = TestUtils.buildMoon();
        given(repository.existsByName(anyString())).willReturn(true);
        // when
        AppException exception = catchThrowableOfType(AppException.class, () -> service.add(entity));
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.EXISTS_NAME);
        then(repository).should().existsByName(entity.getName());
        then(repository).should(never()).existsByNameAndIdNot(anyString(), any());
        then(repository).should(never()).save(any());
        then(eventPublisher).shouldHaveNoInteractions();
    }

    @Test
    void testAdd_planetNotFound() {
        // given
        Moon entity = TestUtils.buildMoon();
        given(repository.existsByName(anyString())).willReturn(false);
        given(planetRepository.existsById(any())).willReturn(false);
        // when
        AppException exception = catchThrowableOfType(AppException.class, () -> service.add(entity));
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_PLANET);
        then(planetRepository).should().existsById(entity.getPlanetId());
        then(repository).should(never()).save(any());
        then(eventPublisher).shouldHaveNoInteractions();
    }

    @Test
    void testAdd_successfulAdd() throws AppException {
        // given
        Moon entity = TestUtils.buildMoon();
        given(repository.existsByName(anyString())).willReturn(false);
        given(planetRepository.existsById(any())).willReturn(true);
        given(repository.save(any())).willReturn(entity);
        // when
        Moon result = service.add(entity);
        // then
        assertThat(result).isEqualTo(entity);
        then(repository).should().save(entity);
        then(eventPublisher).should().publish(EventType.MOON_ADD, entity.getId());
    }

    @Test
    void testUpdate_idNotFound() {
        // given
        Moon entity = TestUtils.buildMoon();
        given(repository.existsById(any())).willReturn(false);
        // when
        AppException exception = catchThrowableOfType(AppException.class, () -> service.update(entity));
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_ENTITY);
        then(repository).should().existsById(entity.getId());
        then(repository).should(never()).save(any());
        then(eventPublisher).shouldHaveNoInteractions();
    }

    @Test
    void testUpdate_duplicateName() {
        // given
        Moon entity = TestUtils.buildMoon();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(true);
        // when
        AppException exception = catchThrowableOfType(AppException.class, () -> service.update(entity));
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.EXISTS_NAME);
        then(repository).should().existsByNameAndIdNot(entity.getName(), entity.getId());
        then(repository).should(never()).existsByName(any());
        then(repository).should(never()).save(any());
        then(eventPublisher).shouldHaveNoInteractions();
    }

    @Test
    void testUpdate_planetNotFound() {
        // given
        Moon entity = TestUtils.buildMoon();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(false);
        given(planetRepository.existsById(any())).willReturn(false);
        // when
        AppException exception = catchThrowableOfType(AppException.class, () -> service.update(entity));
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_PLANET);
        then(planetRepository).should().existsById(entity.getPlanetId());
        then(repository).should(never()).save(any());
        then(eventPublisher).shouldHaveNoInteractions();
    }

    @Test
    void testUpdate_successfulUpdate() throws AppException {
        // given
        Moon entity = TestUtils.buildMoon();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(false);
        given(planetRepository.existsById(any())).willReturn(true);
        given(repository.save(any())).willReturn(entity);
        // when
        Moon result = service.update(entity);
        // then
        assertThat(result).isEqualTo(entity);
        then(repository).should().save(entity);
        then(eventPublisher).should().publish(EventType.MOON_UPDATE, entity.getId());
    }

    @Test
    void testDelete_idNotFound() {
        // given
        UUID id = UUID.randomUUID();
        given(repository.existsById(any())).willReturn(false);
        // when
        AppException exception = catchThrowableOfType(AppException.class, () -> service.delete(id));
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_ENTITY);
        then(repository).should().existsById(id);
        then(repository).should(never()).deleteById(any());
    }

    @Test
    void testDelete_successfulDelete() throws AppException {
        // given
        UUID id = UUID.randomUUID();
        given(repository.existsById(any())).willReturn(true);
        // when
        service.delete(id);
        // then
        then(repository).should().deleteById(id);
        then(eventPublisher).should().publish(EventType.MOON_DELETE, id);
    }
}
