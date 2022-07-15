package com.example.universe.simulator.entityservice.unit.controllers.rest;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.rest.StarRestController;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import com.example.universe.simulator.entityservice.inputs.AddStarInput;
import com.example.universe.simulator.entityservice.inputs.UpdateStarInput;
import com.example.universe.simulator.entityservice.mappers.StarMapper;
import com.example.universe.simulator.entityservice.mappers.StarMapperImpl;
import com.example.universe.simulator.entityservice.services.StarService;
import com.example.universe.simulator.entityservice.specifications.StarSpecificationBuilder;
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

@WebMvcTest(StarRestController.class)
@Import({StarMapperImpl.class})
class StarRestControllerTest extends AbstractWebMvcTest {

    @MockBean
    private StarService service;

    @MockBean
    private StarSpecificationBuilder specificationBuilder;

    @SpyBean
    private StarMapper mapper;

    @Test
    void testGetStars() throws Exception {
        // given
        List<Star> entityList = List.of(
            TestUtils.buildStar()
        );

        StarFilter filter = TestUtils.buildStarFilter();
        Pageable pageable = TestUtils.getDefaultPageable();
        Page<Star> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<StarDto> dtoPage = entityPage.map(mapper::toDto);

        given(service.getList(any(), any())).willReturn(entityPage);
        // when
        MockHttpServletResponse response = performRequest(get("/stars")
            .param("name", filter.getName())
        );
        // then
        verifySuccessfulResponse(response, dtoPage);
        then(specificationBuilder).should().build(filter);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGetStar() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        Star entity = TestUtils.buildStar();
        StarDto dto = mapper.toDto(entity);
        given(service.get(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequest(get("/stars/{id}", id));
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAddStar() throws Exception {
        // given
        AddStarInput input = TestUtils.buildAddStarInput();
        Star entity = mapper.toEntity(input);
        StarDto dto = mapper.toDto(entity);
        given(service.add(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequestWithBody(post("/stars"), input);
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().add(entity);
    }

    @Test
    void testUpdateStar() throws Exception {
        // given
        UpdateStarInput input = TestUtils.buildUpdateStarInput();
        Star entity = mapper.toEntity(input);
        StarDto dto = mapper.toDto(entity);
        given(service.update(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequestWithBody(put("/stars"), input);
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().update(entity);
    }

    @Test
    void testDeleteStar() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        // when
        MockHttpServletResponse response = performRequest(delete("/stars/{id}", id));
        // then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}
