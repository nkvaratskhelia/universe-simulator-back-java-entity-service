package com.example.universe.simulator.entityservice.unit.controllers.rest;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.rest.MoonRestController;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapperImpl;
import com.example.universe.simulator.entityservice.mappers.MoonMapper;
import com.example.universe.simulator.entityservice.mappers.MoonMapperImpl;
import com.example.universe.simulator.entityservice.mappers.PlanetMapperImpl;
import com.example.universe.simulator.entityservice.mappers.StarMapperImpl;
import com.example.universe.simulator.entityservice.services.MoonService;
import com.example.universe.simulator.entityservice.specifications.MoonSpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.MoonDtoValidator;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(MoonRestController.class)
@Import({GalaxyMapperImpl.class, StarMapperImpl.class, PlanetMapperImpl.class, MoonMapperImpl.class})
class MoonRestControllerTest extends AbstractWebMvcTest {

    @MockBean
    private MoonService service;

    @MockBean
    private MoonDtoValidator validator;

    @MockBean
    private MoonSpecificationBuilder specificationBuilder;

    @SpyBean
    private MoonMapper mapper;

    @Test
    void testGetList() throws Exception {
        // given
        List<Moon> entityList = List.of(
            TestUtils.buildMoon()
        );

        MoonFilter filter = TestUtils.buildMoonFilter();
        Pageable pageable = TestUtils.getDefaultPageable();
        Page<Moon> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<MoonDto> dtoPage = entityPage.map(mapper::toDto);

        given(service.getList(any(), any())).willReturn(entityPage);
        // when
        MockHttpServletResponse response = performRequest(get("/moons")
            .param("name", filter.getName())
        );
        // then
        verifySuccessfulResponse(response, dtoPage);
        then(specificationBuilder).should().build(filter);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGet() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        Moon entity = TestUtils.buildMoon();
        MoonDto dto = mapper.toDto(entity);
        given(service.get(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequest(get("/moons/{id}", id));
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAdd() throws Exception {
        // given
        MoonDto inputDto = TestUtils.buildMoonDtoForAdd();
        Moon entity = mapper.toEntity(inputDto);
        MoonDto resultDto = mapper.toDto(entity);
        given(service.add(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequestWithBody(post("/moons"), inputDto);
        // then
        verifySuccessfulResponse(response, resultDto);
        then(validator).should().validate(inputDto, false);
        then(service).should().add(entity);
    }

    @Test
    void testUpdate() throws Exception {
        // given
        MoonDto inputDto = TestUtils.buildMoonDtoForUpdate();
        Moon entity = mapper.toEntity(inputDto);
        MoonDto resultDto = mapper.toDto(entity);
        given(service.update(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequestWithBody(put("/moons"), inputDto);
        // then
        verifySuccessfulResponse(response, resultDto);
        then(validator).should().validate(inputDto, true);
        then(service).should().update(entity);
    }

    @Test
    void testDelete() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        // when
        MockHttpServletResponse response = performRequest(delete("/moons/{id}", id));
        // then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}
