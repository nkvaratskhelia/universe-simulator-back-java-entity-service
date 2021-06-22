package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.StarController;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.services.StarService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.validators.StarDtoValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

@WebMvcTest(StarController.class)
class StarControllerTest extends AbstractWebMvcTest {

    @MockBean
    private StarDtoValidator validator;

    @MockBean
    private StarService service;

    @Test
    void testGetList() throws Exception {
        // given
        List<Star> entityList = List.of(
            TestUtils.buildStar()
        );

        Pageable pageable = TestUtils.getSpaceEntityPageable();
        Page<Star> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<StarDto> dtoPage = entityPage.map(item -> modelMapper.map(item, StarDto.class));

        given(service.getList(any(), any())).willReturn(entityPage);
        // when
        MockHttpServletResponse response = performRequest(post("/star/get-list")
            .param("page", String.valueOf(pageable.getPageNumber()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .param("sort", "version,desc")
            .param("sort", "name,asc")
        );
        // then
        verifySuccessfulResponse(response, dtoPage);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGet() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        Star entity = TestUtils.buildStar();
        StarDto dto = modelMapper.map(entity, StarDto.class);
        given(service.get(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequest(get("/star/get/{id}", id));
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAdd() throws Exception {
        // given
        StarDto inputDto = TestUtils.buildStarDtoForAdd();
        Star entity = modelMapper.map(inputDto, Star.class);
        StarDto resultDto = modelMapper.map(entity, StarDto.class);
        given(service.add(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequest(post("/star/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputDto))
        );
        // then
        verifySuccessfulResponse(response, resultDto);
        then(validator).should().validate(inputDto, false);
        then(service).should().add(entity);
    }

    @Test
    void testUpdate() throws Exception {
        // given
        StarDto inputDto = TestUtils.buildStarDtoForUpdate();
        Star entity = modelMapper.map(inputDto, Star.class);
        StarDto resultDto = modelMapper.map(entity, StarDto.class);
        given(service.update(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequest(put("/star/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputDto))
        );
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
        MockHttpServletResponse response = performRequest(delete("/star/delete/{id}", id));
        // then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}
