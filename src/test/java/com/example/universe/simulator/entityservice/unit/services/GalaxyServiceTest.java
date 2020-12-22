package com.example.universe.simulator.entityservice.unit.services;

import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.repositories.GalaxyRepository;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
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
        then(repository).should().findAll();
        assertEquals(list, result);
    }

    @Test
    void testGet_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        given(repository.findById(any())).willReturn(Optional.empty());
        //then
        assertThrows(NoSuchElementException.class, () -> service.get(id));
        then(repository).should().findById(id);
    }

    @Test
    void testGet_successfulGet() {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().name("name").build();
        given(repository.findById(any())).willReturn(Optional.of(entity));
        //when
        Galaxy result = service.get(id);
        //then
        then(repository).should().findById(id);
        assertEquals(entity, result);
    }

    @Test
    void testAdd() {
        //given
        Galaxy entity = Galaxy.builder().name("name").build();
        given(repository.save(any())).willReturn(entity);
        //when
        Galaxy result = service.add(entity);
        //then
        then(repository).should().save(entity);
        assertEquals(entity, result);
    }

    @Test
    void testUpdate_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().id(id).name("name").build();
        given(repository.findById(any())).willReturn(Optional.empty());
        //then
        assertThrows(NoSuchElementException.class, () -> service.update(entity));
        then(repository).should().findById(id);
        then(repository).should(never()).save(any());
    }

    @Test
    void testUpdate_successfulUpdate() {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = Galaxy.builder().id(id).name("name").build();
        given(repository.findById(any())).willReturn(Optional.of(entity));
        given(repository.save(any())).willReturn(entity);
        //when
        Galaxy result = service.update(entity);
        //then
        then(repository).should().findById(id);
        then(repository).should().save(entity);
        assertEquals(entity, result);
    }

    @Test
    void testDelete_idNotFound() {
        //given
        UUID id = UUID.randomUUID();
        given(repository.findById(any())).willReturn(Optional.empty());
        //then
        assertThrows(NoSuchElementException.class, () -> service.delete(id));
        then(repository).should().findById(id);
        then(repository).should(never()).delete(any());
    }

    @Test
    void testDelete_successfulDelete() {
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
