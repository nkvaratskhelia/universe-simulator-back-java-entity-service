package com.example.universe.simulator.entityservice.unit.services;

import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class GalaxyServiceTest {

    @Mock
    private GalaxyRepository repository;

    @InjectMocks
    private GalaxyService service;

    @Test
    void testGetList() {
        //given
        List<Galaxy> list = List.of(
                Galaxy.builder().name("name").build()
        );
        given(repository.findAll()).willReturn(list);
        //when
        List<Galaxy> result = service.getList();
        //then
        assertEquals(list, result);
        then(repository).should().findAll();
    }

    @Test
    void testGet_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        given(repository.findById(any())).willReturn(Optional.empty());
        //then
        AppException exception = assertThrows(AppException.class, () -> service.get(id));
        assertEquals(ErrorCodeType.ENTITY_NOT_FOUND, exception.getErrorCode());

        then(repository).should().findById(id);
    }

    @Test
    void testGet_successfulGet() throws AppException {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().name("name").build();
        given(repository.findById(any())).willReturn(Optional.of(entity));
        //when
        Galaxy result = service.get(id);
        //then
        assertEquals(entity, result);
        then(repository).should().findById(id);
    }

    @Test
    void testAdd() {
        //given
        Galaxy entity = Galaxy.builder().name("name").build();
        given(repository.save(any())).willReturn(entity);
        //when
        Galaxy result = service.add(entity);
        //then
        assertEquals(entity, result);
        then(repository).should().save(entity);
    }

    @Test
    void testUpdate_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().id(id).name("name").build();
        given(repository.findById(any())).willReturn(Optional.empty());
        //then
        AppException exception = assertThrows(AppException.class, () -> service.update(entity));
        assertEquals(ErrorCodeType.ENTITY_NOT_FOUND, exception.getErrorCode());

        then(repository).should().findById(id);
        then(repository).should(never()).save(any());
    }

    @Test
    void testUpdate_successfulUpdate() throws AppException {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().id(id).name("name").build();
        given(repository.findById(any())).willReturn(Optional.of(entity));
        given(repository.save(any())).willReturn(entity);
        //when
        Galaxy result = service.update(entity);
        //then
        assertEquals(entity, result);
        then(repository).should().findById(id);
        then(repository).should().save(entity);
    }

    @Test
    void testDelete_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        given(repository.findById(any())).willReturn(Optional.empty());
        //then
        AppException exception = assertThrows(AppException.class, () -> service.delete(id));
        assertEquals(ErrorCodeType.ENTITY_NOT_FOUND, exception.getErrorCode());

        then(repository).should().findById(id);
        then(repository).should(never()).delete(any());
    }

    @Test
    void testDelete_successfulDelete() throws AppException {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().name("name").build();
        given(repository.findById(any())).willReturn(Optional.of(entity));
        //when
        service.delete(id);
        //then
        then(repository).should().findById(id);
        then(repository).should().deleteById(id);
    }
}
