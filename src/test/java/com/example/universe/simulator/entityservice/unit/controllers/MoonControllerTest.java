package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.MoonController;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.services.MoonService;
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

@WebMvcTest(MoonController.class)
class MoonControllerTest extends AbstractWebMvcTest {

    @MockBean
    private MoonService service;

    @Test
    void testGetList() throws Exception {
        //given
        List<Moon> entityList = List.of(
            Moon.builder().name("name").build()
        );

        Sort sort = Sort.by(
            Sort.Order.desc("version"),
            Sort.Order.asc("name")
        );
        Pageable pageable = PageRequest.of(1, 2, sort);
        Page<Moon> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<MoonDto> dtoPage = entityPage.map(item -> modelMapper.map(item, MoonDto.class));

        given(service.getList(any(), any())).willReturn(entityPage);
        //when
        MockHttpServletResponse response = performRequest(post("/moon/get-list")
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
        Moon entity = Moon.builder().name("name").build();
        MoonDto dto = modelMapper.map(entity, MoonDto.class);
        given(service.get(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = performRequest(get("/moon/get/{id}", id));
        //then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAdd() throws Exception {
        //given
        MoonDto inputDto = TestUtils.buildMoonDtoForAdd();
        Moon entity = modelMapper.map(inputDto, Moon.class);
        MoonDto resultDto = modelMapper.map(entity, MoonDto.class);
        given(service.add(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = performRequest(post("/moon/add")
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
        MoonDto inputDto = TestUtils.buildMoonDtoForUpdate();
        Moon entity = modelMapper.map(inputDto, Moon.class);
        MoonDto resultDto = modelMapper.map(entity, MoonDto.class);
        given(service.update(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = performRequest(put("/moon/update")
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
        MockHttpServletResponse response = performRequest(delete("/moon/delete/{id}", id));
        //then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}
