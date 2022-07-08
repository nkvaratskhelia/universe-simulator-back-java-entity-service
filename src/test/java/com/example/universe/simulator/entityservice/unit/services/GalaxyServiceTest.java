package com.example.universe.simulator.entityservice.unit.services;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.events.EventPublisher;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.repositories.StarRepository;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import com.example.universe.simulator.entityservice.types.EventType;
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
class GalaxyServiceTest {

    @Mock
    private GalaxyRepository repository;

    @Mock
    private StarRepository starRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private GalaxyService service;

    @Test
    void testGetList() {
        // given
        List<Galaxy> list = List.of(
            TestUtils.buildGalaxy()
        );
        Pageable pageable = Pageable.unpaged();
        Page<Galaxy> page = new PageImpl<>(list, pageable, list.size());

        given(repository.findAll(ArgumentMatchers.<Specification<Galaxy>>any(), any(Pageable.class)))
            .willReturn(page);
        // when
        Page<Galaxy> result = service.getList(null, pageable);
        // then
        assertThat(result).isEqualTo(page);
        then(repository).should().findAll((Specification<Galaxy>) null, pageable);
    }

    @Test
    void testGet_idNotFound() {
        // given
        UUID id = UUID.randomUUID();
        given(repository.findById(any())).willReturn(Optional.empty());
        // when
        AppException exception = catchThrowableOfType(() -> service.get(id), AppException.class);
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_ENTITY);
        then(repository).should().findById(id);
    }

    @Test
    void testGet_successfulGet() throws AppException {
        // given
        UUID id = UUID.randomUUID();
        Galaxy entity = TestUtils.buildGalaxy();
        given(repository.findById(any())).willReturn(Optional.of(entity));
        // when
        Galaxy result = service.get(id);
        // then
        assertThat(result).isEqualTo(entity);
        then(repository).should().findById(id);
    }

    @Test
    void testAdd_duplicateName() {
        // given
        Galaxy entity = TestUtils.buildGalaxy();
        given(repository.existsByName(anyString())).willReturn(true);
        // when
        AppException exception = catchThrowableOfType(() -> service.add(entity), AppException.class);
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.EXISTS_NAME);
        then(repository).should().existsByName(entity.getName());
        then(repository).should(never()).existsByNameAndIdNot(anyString(), any());
        then(repository).should(never()).save(any());
        then(eventPublisher).shouldHaveNoInteractions();
    }

    @Test
    void testAdd_successfulAdd() throws AppException {
        // given
        Galaxy entity = TestUtils.buildGalaxy();
        given(repository.existsByName(anyString())).willReturn(false);
        given(repository.save(any())).willReturn(entity);
        // when
        Galaxy result = service.add(entity);
        // then
        assertThat(result).isEqualTo(entity);
        then(repository).should().save(entity);
        then(eventPublisher).should().publish(EventType.GALAXY_ADD, entity.getId());
    }

    @Test
    void testUpdate_idNotFound() {
        // given
        Galaxy entity = TestUtils.buildGalaxy();
        given(repository.existsById(any())).willReturn(false);
        // when
        AppException exception = catchThrowableOfType(() -> service.update(entity), AppException.class);
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.NOT_FOUND_ENTITY);
        then(repository).should().existsById(entity.getId());
        then(repository).should(never()).save(any());
        then(eventPublisher).shouldHaveNoInteractions();
    }

    @Test
    void testUpdate_duplicateName() {
        // given
        Galaxy entity = TestUtils.buildGalaxy();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(true);
        // when
        AppException exception = catchThrowableOfType(() -> service.update(entity), AppException.class);
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.EXISTS_NAME);
        then(repository).should().existsByNameAndIdNot(entity.getName(), entity.getId());
        then(repository).should(never()).existsByName(any());
        then(repository).should(never()).save(any());
        then(eventPublisher).shouldHaveNoInteractions();
    }

    @Test
    void testUpdate_successfulUpdate() throws AppException {
        // given
        Galaxy entity = TestUtils.buildGalaxy();
        given(repository.existsById(any())).willReturn(true);
        given(repository.existsByNameAndIdNot(anyString(), any())).willReturn(false);
        given(repository.save(any())).willReturn(entity);
        // when
        Galaxy result = service.update(entity);
        // then
        assertThat(result).isEqualTo(entity);
        then(repository).should().save(entity);
        then(eventPublisher).should().publish(EventType.GALAXY_UPDATE, entity.getId());
    }

    @Test
    void testDelete_inUse() {
        // given
        UUID id = UUID.randomUUID();
        given(starRepository.existsByGalaxyId(any())).willReturn(true);
        // when
        AppException exception = catchThrowableOfType(() -> service.delete(id), AppException.class);
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.IN_USE);
        then(starRepository).should().existsByGalaxyId(id);
        then(repository).shouldHaveNoInteractions();
        then(eventPublisher).shouldHaveNoInteractions();
    }

    @Test
    void testDelete_successfulDelete() throws AppException {
        // given
        UUID id = UUID.randomUUID();
        given(starRepository.existsByGalaxyId(any())).willReturn(false);
        // when
        service.delete(id);
        // then
        then(repository).should().deleteById(id);
        then(eventPublisher).should().publish(EventType.GALAXY_DELETE, id);
    }
}
