package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.GalaxyController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.validators.GalaxyDtoValidator;
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

@WebMvcTest(GalaxyController.class)
class GalaxyControllerTest extends AbstractWebMvcTest {

    @MockBean
    private GalaxyDtoValidator validator;

    @MockBean
    private GalaxyService service;

    @Test
    void testGetList() throws Exception {
        // given
        List<Galaxy> entityList = List.of(
            TestUtils.buildGalaxy()
        );

        Pageable pageable = TestUtils.getSpaceEntityPageable();
        Page<Galaxy> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<GalaxyDto> dtoPage = entityPage.map(item -> modelMapper.map(item, GalaxyDto.class));

        given(service.getList(any(), any())).willReturn(entityPage);
        // when
        MockHttpServletResponse response = performRequest(post("/galaxy/get-list")
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
        Galaxy entity = TestUtils.buildGalaxy();
        GalaxyDto dto = modelMapper.map(entity, GalaxyDto.class);
        given(service.get(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequest(get("/galaxy/get/{id}", id));
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAdd() throws Exception {
        // given
        GalaxyDto inputDto = TestUtils.buildGalaxyDtoForAdd();
        Galaxy entity = modelMapper.map(inputDto, Galaxy.class);
        GalaxyDto resultDto = modelMapper.map(entity, GalaxyDto.class);
        given(service.add(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequest(post("/galaxy/add")
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
        GalaxyDto inputDto = TestUtils.buildGalaxyDtoForUpdate();
        Galaxy entity = modelMapper.map(inputDto, Galaxy.class);
        GalaxyDto resultDto = modelMapper.map(entity, GalaxyDto.class);
        given(service.update(any())).willReturn(entity);
        // when
        MockHttpServletResponse response = performRequest(put("/galaxy/update")
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
        MockHttpServletResponse response = performRequest(delete("/galaxy/delete/{id}", id));
        // then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}
