package com.example.universe.simulator.entityservice.unit.controllers.rest;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.rest.MoonRestController;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.inputs.AddMoonInput;
import com.example.universe.simulator.entityservice.inputs.UpdateMoonInput;
import com.example.universe.simulator.entityservice.mappers.MoonMapper;
import com.example.universe.simulator.entityservice.mappers.MoonMapperImpl;
import com.example.universe.simulator.entityservice.services.MoonService;
import com.example.universe.simulator.entityservice.specifications.MoonSpecificationBuilder;
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

@WebMvcTest(MoonRestController.class)
@Import({MoonMapperImpl.class})
class MoonRestControllerTest extends AbstractWebMvcTest {

    @MockitoBean
    private MoonService service;

    @MockitoBean
    private MoonSpecificationBuilder specificationBuilder;

    @MockitoSpyBean
    private MoonMapper mapper;

    @Test
    void testGetMoons() throws Exception {
        // given
        List<Moon> entityList = List.of(
            TestUtils.buildMoon()
        );

        MoonFilter filter = TestUtils.buildMoonFilter();
        Pageable pageable = TestUtils.buildDefaultPageable();
        Page<Moon> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<MoonDto> dtoPage = entityPage.map(mapper::toDto);

        given(service.getList(any(), eq(pageable))).willReturn(entityPage);
        // when
        MockHttpServletResponse response = performRequest(get("/moons")
            .param("name", filter.getName())
        );
        // then
        verifySuccessfulResponse(response, dtoPage);
        then(specificationBuilder).should().build(filter);
    }

    @Test
    void testGetMoon() throws Exception {
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
    void testAddMoon() throws Exception {
        // given
        AddMoonInput input = TestUtils.buildAddMoonInput();
        Moon entity = mapper.toEntity(input);
        MoonDto dto = mapper.toDto(entity);
        given(service.add(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequestWithBody(post("/moons"), input);
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().add(entity);
    }

    @Test
    void testUpdateMoon() throws Exception {
        // given
        UpdateMoonInput input = TestUtils.buildUpdateMoonInput();
        Moon entity = mapper.toEntity(input);
        MoonDto dto = mapper.toDto(entity);
        given(service.update(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequestWithBody(put("/moons"), input);
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().update(entity);
    }

    @Test
    void testDeleteMoon() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        // when
        MockHttpServletResponse response = performRequest(delete("/moons/{id}", id));
        // then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}
