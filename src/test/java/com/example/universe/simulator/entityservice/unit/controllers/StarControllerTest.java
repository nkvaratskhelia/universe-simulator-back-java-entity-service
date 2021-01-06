package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.controllers.StarController;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.services.StarService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.utils.TestUtils;
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
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(StarController.class)
class StarControllerTest extends AbstractWebMvcTest {

    @MockBean
    private StarService service;

    @Test
    void testGetList() throws Exception {
        //given
        List<Star> entityList = List.of(
                Star.builder().name("name").build()
        );

        Sort sort = Sort.by(
                Sort.Order.desc("version"),
                Sort.Order.asc("name")
        );
        Pageable pageable = PageRequest.of(1, 2, sort);
        Page<Star> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<StarDto> dtoPage = entityPage.map(item -> modelMapper.map(item, StarDto.class));

        given(service.getList(any())).willReturn(entityPage);
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/star/get-list")
                .param("page", "1")
                .param("size", "2")
                .param("sort", "version,desc")
                .param("sort", "name,asc")
        ).andReturn().getResponse();
        //then
        verifySuccessfulResponse(response, dtoPage);
        then(service).should().getList(pageable);
    }

    @Test
    void testGet() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        Star entity = Star.builder().name("name").build();
        StarDto dto = modelMapper.map(entity, StarDto.class);
        given(service.get(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/star/get/{id}", id)).andReturn().getResponse();
        //then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAdd_validate_nullName() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.setName(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validate_emptyName() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.setName("");
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validate_BlankName() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.setName(" ");
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validate_nullGalaxy() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.setGalaxy(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_GALAXY);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validate_nullGalaxyId() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.getGalaxy().setId(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_GALAXY_ID);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_dirtyFieldFixAndSuccessfulAdd() throws Exception {
        //given
        StarDto inputDto = TestUtils.buildStarDtoForAdd();
        Star entity = modelMapper.map(inputDto, Star.class);
        StarDto resultDto = modelMapper.map(entity, StarDto.class);

        //dirty input
        inputDto.setId(UUID.randomUUID());
        inputDto.setName(" name ");
        inputDto.setVersion(1L);

        given(service.add(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto))
        ).andReturn().getResponse();
        //then
        verifySuccessfulResponse(response, resultDto);
        then(service).should().add(entity);
    }

    @Test
    void testUpdate_validate_nullId() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForUpdate();
        dto.setId(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_ID);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validate_nullName() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForUpdate();
        dto.setName(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validate_EmptyName() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForUpdate();
        dto.setName("");
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validate_BlankName() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForUpdate();
        dto.setName(" ");
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validate_nullVersion() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForUpdate();
        dto.setVersion(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_VERSION);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validate_nullGalaxy() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForUpdate();
        dto.setGalaxy(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_GALAXY);
        then(service).should(never()).add(any());
    }

    @Test
    void testUpdate_validate_nullGalaxyId() throws Exception {
        //given
        StarDto dto = TestUtils.buildStarDtoForUpdate();
        dto.getGalaxy().setId(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_GALAXY_ID);
        then(service).should(never()).add(any());
    }

    @Test
    void testUpdate_dirtyFieldFixAndSuccessfulUpdate() throws Exception {
        //given
        StarDto inputDto = TestUtils.buildStarDtoForUpdate();
        Star entity = modelMapper.map(inputDto, Star.class);
        StarDto resultDto = modelMapper.map(entity, StarDto.class);

        //dirty input
        inputDto.setName(" name ");

        given(service.update(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto))
        ).andReturn().getResponse();
        //then
        verifySuccessfulResponse(response, resultDto);
        then(service).should().update(entity);
    }

    @Test
    void testDelete() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/star/delete/{id}", id)).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}