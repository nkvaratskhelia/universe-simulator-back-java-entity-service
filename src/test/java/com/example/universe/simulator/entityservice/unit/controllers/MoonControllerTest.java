package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.controllers.MoonController;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.services.MoonService;
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
        MockHttpServletResponse response = mockMvc.perform(post("/moon/get-list")
                .param("page", "1")
                .param("size", "2")
                .param("sort", "version,desc")
                .param("sort", "name,asc")
        ).andReturn().getResponse();
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
        MockHttpServletResponse response = mockMvc.perform(get("/moon/get/{id}", id)).andReturn().getResponse();
        //then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAdd_validate_nullName() throws Exception {
        //given
        MoonDto dto = TestUtils.buildMoonDtoForAdd();
        dto.setName(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/moon/add")
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
        MoonDto dto = TestUtils.buildMoonDtoForAdd();
        dto.setName("");
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/moon/add")
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
        MoonDto dto = TestUtils.buildMoonDtoForAdd();
        dto.setName(" ");
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/moon/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validate_nullPlanet() throws Exception {
        //given
        MoonDto dto = TestUtils.buildMoonDtoForAdd();
        dto.setPlanet(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/moon/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_PLANET);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validate_nullPlanetId() throws Exception {
        //given
        MoonDto dto = TestUtils.buildMoonDtoForAdd();
        dto.getPlanet().setId(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/moon/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_PLANET_ID);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_dirtyFieldFixAndSuccessfulAdd() throws Exception {
        //given
        MoonDto inputDto = TestUtils.buildMoonDtoForAdd();
        Moon entity = modelMapper.map(inputDto, Moon.class);
        MoonDto resultDto = modelMapper.map(entity, MoonDto.class);

        //dirty input
        inputDto.setId(UUID.randomUUID());
        inputDto.setName(" name ");
        inputDto.setVersion(1L);

        given(service.add(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/moon/add")
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
        MoonDto dto = TestUtils.buildMoonDtoForUpdate();
        dto.setId(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/moon/update")
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
        MoonDto dto = TestUtils.buildMoonDtoForUpdate();
        dto.setName(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/moon/update")
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
        MoonDto dto = TestUtils.buildMoonDtoForUpdate();
        dto.setName("");
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/moon/update")
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
        MoonDto dto = TestUtils.buildMoonDtoForUpdate();
        dto.setName(" ");
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/moon/update")
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
        MoonDto dto = TestUtils.buildMoonDtoForUpdate();
        dto.setVersion(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/moon/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_VERSION);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validate_nullPlanet() throws Exception {
        //given
        MoonDto dto = TestUtils.buildMoonDtoForUpdate();
        dto.setPlanet(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/moon/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_PLANET);
        then(service).should(never()).add(any());
    }

    @Test
    void testUpdate_validate_nullPlanetId() throws Exception {
        //given
        MoonDto dto = TestUtils.buildMoonDtoForUpdate();
        dto.getPlanet().setId(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/moon/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_PLANET_ID);
        then(service).should(never()).add(any());
    }

    @Test
    void testUpdate_dirtyFieldFixAndSuccessfulUpdate() throws Exception {
        //given
        MoonDto inputDto = TestUtils.buildMoonDtoForUpdate();
        Moon entity = modelMapper.map(inputDto, Moon.class);
        MoonDto resultDto = modelMapper.map(entity, MoonDto.class);

        //dirty input
        inputDto.setName(" name ");

        given(service.update(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/moon/update")
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
        MockHttpServletResponse response = mockMvc.perform(delete("/moon/delete/{id}", id)).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}
