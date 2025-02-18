package com.example.universe.simulator.entityservice.unit.controllers.rest;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.rest.PlanetRestController;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.inputs.AddPlanetInput;
import com.example.universe.simulator.entityservice.inputs.UpdatePlanetInput;
import com.example.universe.simulator.entityservice.mappers.PlanetMapper;
import com.example.universe.simulator.entityservice.mappers.PlanetMapperImpl;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.specifications.PlanetSpecificationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(PlanetRestController.class)
@Import({PlanetMapperImpl.class})
class PlanetRestControllerTest extends AbstractWebMvcTest {

    @MockitoBean
    private PlanetService service;

    @MockitoBean
    private PlanetSpecificationBuilder specificationBuilder;

    @MockitoSpyBean
    private PlanetMapper mapper;

    @Test
    void testGetPlanets() throws Exception {
        // given
        List<Planet> entityList = List.of(
            TestUtils.buildPlanet()
        );

        PlanetFilter filter = TestUtils.buildPlanetFilter();
        Pageable pageable = TestUtils.buildDefaultPageable();
        Page<Planet> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<PlanetDto> dtoPage = entityPage.map(mapper::toDto);

        given(service.getList(any(), eq(pageable))).willReturn(entityPage);
        // when
        MockHttpServletResponse response = performRequest(get("/planets")
            .param("name", filter.getName())
        );
        // then
        verifySuccessfulResponse(response, dtoPage);
        then(specificationBuilder).should().build(filter);
    }

    @Test
    void testGetPlanet() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        Planet entity = TestUtils.buildPlanet();
        PlanetDto dto = mapper.toDto(entity);
        given(service.get(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequest(get("/planets/{id}", id));
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAddPlanet() throws Exception {
        // given
        AddPlanetInput input = TestUtils.buildAddPlanetInput();
        Planet entity = mapper.toEntity(input);
        PlanetDto dto = mapper.toDto(entity);
        given(service.add(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequestWithBody(post("/planets"), input);
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().add(entity);
    }

    @Test
    void testUpdatePlanet() throws Exception {
        // given
        UpdatePlanetInput input = TestUtils.buildUpdatePlanetInput();
        Planet entity = mapper.toEntity(input);
        PlanetDto dto = mapper.toDto(entity);
        given(service.update(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequestWithBody(put("/planets"), input);
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().update(entity);
    }

    @Test
    void testDeletePlanet() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        // when
        MockHttpServletResponse response = performRequest(delete("/planets/{id}", id));
        // then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}
