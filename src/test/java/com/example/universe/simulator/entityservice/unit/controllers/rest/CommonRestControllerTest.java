package com.example.universe.simulator.entityservice.unit.controllers.rest;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.rest.GalaxyRestController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

// Common controller cases are tested using GalaxyController.
@WebMvcTest(GalaxyRestController.class)
@Import(GalaxyMapperImpl.class)
class CommonRestControllerTest extends AbstractWebMvcTest {

    @MockBean
    private GalaxyService service;

    @MockBean
    private GalaxySpecificationBuilder specificationBuilder;

    @SpyBean
    private GalaxyMapperImpl mapper;

    @Test
    void testGetGalaxies_defaultPageable() throws Exception {
        // given
        List<Galaxy> entityList = List.of(
            TestUtils.buildGalaxy()
        );

        Pageable pageable = TestUtils.getDefaultPageable();
        Page<Galaxy> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<GalaxyDto> dtoPage = entityPage.map(mapper::toDto);

        given(service.getList(any(), any())).willReturn(entityPage);
        // when
        MockHttpServletResponse response = performRequest(get("/galaxies"));
        // then
        verifySuccessfulResponse(response, dtoPage);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGetGalaxies_customPageable() throws Exception {
        // given
        List<Galaxy> entityList = List.of(
            TestUtils.buildGalaxy()
        );

        Pageable pageable = TestUtils.getSpaceEntityPageable();
        Page<Galaxy> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<GalaxyDto> dtoPage = entityPage.map(mapper::toDto);

        given(service.getList(any(), any())).willReturn(entityPage);
        // when
        MockHttpServletResponse response = performRequest(get("/galaxies")
            .param("page", String.valueOf(pageable.getPageNumber()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .param("sort", "version,desc")
            .param("sort", "name,asc")
        );
        // then
        verifySuccessfulResponse(response, dtoPage);
        then(service).should().getList(null, pageable);
    }
}
