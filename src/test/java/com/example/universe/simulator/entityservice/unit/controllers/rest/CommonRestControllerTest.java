package com.example.universe.simulator.entityservice.unit.controllers.rest;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.rest.GalaxyRestController;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapperImpl;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

// Common rest controller cases are tested using GalaxyRestController.
@WebMvcTest(GalaxyRestController.class)
@Import(GalaxyMapperImpl.class)
class CommonRestControllerTest extends AbstractWebMvcTest {

    @MockitoBean
    private GalaxyService galaxyService;

    @MockitoBean
    private GalaxySpecificationBuilder galaxySpecificationBuilder;

    @Test
    void testPaging_defaultPageable() throws Exception {
        // given
        Pageable pageable = TestUtils.buildDefaultPageable();

        given(galaxyService.getList(any(), any())).willReturn(Page.empty());
        // when
        performRequest(get("/galaxies"));
        // then
        then(galaxyService).should().getList(any(), eq(pageable));
    }

    @Test
    void testPaging_customPageable() throws Exception {
        // given
        Pageable pageable = TestUtils.buildSpaceEntityPageable();

        given(galaxyService.getList(any(), any())).willReturn(Page.empty());
        // when
        performRequest(get("/galaxies")
            .param("page", String.valueOf(pageable.getPageNumber()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .param("sort", "version,desc")
            .param("sort", "name,asc")
        );
        // then
        then(galaxyService).should().getList(any(), eq(pageable));
    }
}
