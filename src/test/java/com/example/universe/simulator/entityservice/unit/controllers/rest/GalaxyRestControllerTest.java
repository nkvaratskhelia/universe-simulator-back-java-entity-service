package com.example.universe.simulator.entityservice.unit.controllers.rest;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.rest.GalaxyRestController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.inputs.AddGalaxyInput;
import com.example.universe.simulator.entityservice.inputs.UpdateGalaxyInput;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapper;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapperImpl;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletResponse;

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

@WebMvcTest(GalaxyRestController.class)
@Import(GalaxyMapperImpl.class)
class GalaxyRestControllerTest extends AbstractWebMvcTest {

    @MockBean
    private GalaxyService service;

    @MockBean
    private GalaxySpecificationBuilder specificationBuilder;

    @SpyBean
    private GalaxyMapper mapper;

    @Test
    void testGetGalaxies() throws Exception {
        // given
        List<Galaxy> entityList = List.of(
            TestUtils.buildGalaxy()
        );

        GalaxyFilter filter = TestUtils.buildGalaxyFilter();
        Pageable pageable = TestUtils.buildDefaultPageable();
        Page<Galaxy> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<GalaxyDto> dtoPage = entityPage.map(mapper::toDto);

        given(service.getList(any(), eq(pageable))).willReturn(entityPage);
        // when
        MockHttpServletResponse response = performRequest(get("/galaxies")
            .param("name", filter.getName())
        );
        // then
        verifySuccessfulResponse(response, dtoPage);
        then(specificationBuilder).should().build(filter);
    }

    @Test
    void testGetGalaxy() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        Galaxy entity = TestUtils.buildGalaxy();
        GalaxyDto dto = mapper.toDto(entity);
        given(service.get(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequest(get("/galaxies/{id}", id));
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAddGalaxy() throws Exception {
        // given
        AddGalaxyInput input = TestUtils.buildAddGalaxyInput();
        Galaxy entity = mapper.toEntity(input);
        GalaxyDto dto = mapper.toDto(entity);
        given(service.add(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequestWithBody(post("/galaxies"), input);
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().add(entity);
    }

    @Test
    void testUpdateGalaxy() throws Exception {
        // given
        UpdateGalaxyInput input = TestUtils.buildUpdateGalaxyInput();
        Galaxy entity = mapper.toEntity(input);
        GalaxyDto dto = mapper.toDto(entity);
        given(service.update(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequestWithBody(put("/galaxies"), input);
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().update(entity);
    }

    @Test
    void testDeleteGalaxy() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        // when
        MockHttpServletResponse response = performRequest(delete("/galaxies/{id}", id));
        // then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}
