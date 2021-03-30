package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.PlanetController;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(PlanetController.class)
class PlanetControllerTest extends AbstractWebMvcTest {

    @MockBean
    private PlanetService service;

    @Test
    void testGetList() throws Exception {
        //given
        List<Planet> entityList = List.of(
            TestUtils.buildPlanet()
        );

        Sort sort = Sort.by(
            Sort.Order.desc("version"),
            Sort.Order.asc("name")
        );
        Pageable pageable = PageRequest.of(1, 2, sort);
        Page<Planet> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<PlanetDto> dtoPage = entityPage.map(item -> modelMapper.map(item, PlanetDto.class));

        given(service.getList(any(), any())).willReturn(entityPage);
        //when
        MockHttpServletResponse response = performRequest(post("/planet/get-list")
            .param("page", "1")
            .param("size", "2")
            .param("sort", "version,desc")
            .param("sort", "name,asc")
        );
        //then
        verifySuccessfulResponse(response, dtoPage);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGet() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        Planet entity = TestUtils.buildPlanet();
        PlanetDto dto = modelMapper.map(entity, PlanetDto.class);
        given(service.get(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = performRequest(get("/planet/get/{id}", id));
        //then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAdd() throws Exception {
        //given
        PlanetDto inputDto = TestUtils.buildPlanetDtoForAdd();
        Planet entity = modelMapper.map(inputDto, Planet.class);
        PlanetDto resultDto = modelMapper.map(entity, PlanetDto.class);
        given(service.add(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = performRequest(post("/planet/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputDto))
        );
        //then
        verifySuccessfulResponse(response, resultDto);
        then(service).should().add(entity);
    }

    @Test
    void testUpdate() throws Exception {
        //given
        PlanetDto inputDto = TestUtils.buildPlanetDtoForUpdate();
        Planet entity = modelMapper.map(inputDto, Planet.class);
        PlanetDto resultDto = modelMapper.map(entity, PlanetDto.class);
        given(service.update(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = performRequest(put("/planet/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputDto))
        );
        //then
        verifySuccessfulResponse(response, resultDto);
        then(service).should().update(entity);
    }

    @Test
    void testDelete() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        //when
        MockHttpServletResponse response = performRequest(delete("/planet/delete/{id}", id));
        //then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}
