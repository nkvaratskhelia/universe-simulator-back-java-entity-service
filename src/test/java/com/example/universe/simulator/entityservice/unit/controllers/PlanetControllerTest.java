package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.controllers.PlanetController;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.services.PlanetService;
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

@WebMvcTest(PlanetController.class)
class PlanetControllerTest extends AbstractWebMvcTest {

    @MockBean
    private PlanetService service;

    @Test
    void testGetList() throws Exception {
        //given
        List<Planet> entityList = List.of(
                Planet.builder().name("name").build()
        );

        Sort sort = Sort.by(
                Sort.Order.desc("version"),
                Sort.Order.asc("name")
        );
        Pageable pageable = PageRequest.of(1, 2, sort);
        Page<Planet> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<PlanetDto> dtoPage = entityPage.map(item -> modelMapper.map(item, PlanetDto.class));

        given(service.getList(any())).willReturn(entityPage);
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/planet/get-list")
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
        Planet entity = Planet.builder().name("name").build();
        PlanetDto dto = modelMapper.map(entity, PlanetDto.class);
        given(service.get(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/planet/get/{id}", id)).andReturn().getResponse();
        //then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAdd_validate_nullName() throws Exception {
        //given
        PlanetDto dto = TestUtils.buildPlanetDtoForAdd();
        dto.setName(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/planet/add")
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
        PlanetDto dto = TestUtils.buildPlanetDtoForAdd();
        dto.setName("");
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/planet/add")
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
        PlanetDto dto = TestUtils.buildPlanetDtoForAdd();
        dto.setName(" ");
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/planet/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validate_nullStar() throws Exception {
        //given
        PlanetDto dto = TestUtils.buildPlanetDtoForAdd();
        dto.setStar(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/planet/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_STAR);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validate_nullStarId() throws Exception {
        //given
        PlanetDto dto = TestUtils.buildPlanetDtoForAdd();
        dto.getStar().setId(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/planet/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_STAR_ID);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_dirtyFieldFixAndSuccessfulAdd() throws Exception {
        //given
        PlanetDto inputDto = TestUtils.buildPlanetDtoForAdd();
        Planet entity = modelMapper.map(inputDto, Planet.class);
        PlanetDto resultDto = modelMapper.map(entity, PlanetDto.class);

        //dirty input
        inputDto.setId(UUID.randomUUID());
        inputDto.setName(" name ");
        inputDto.setVersion(1L);

        given(service.add(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/planet/add")
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
        PlanetDto dto = TestUtils.buildPlanetDtoForUpdate();
        dto.setId(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/planet/update")
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
        PlanetDto dto = TestUtils.buildPlanetDtoForUpdate();
        dto.setName(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/planet/update")
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
        PlanetDto dto = TestUtils.buildPlanetDtoForUpdate();
        dto.setName("");
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/planet/update")
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
        PlanetDto dto = TestUtils.buildPlanetDtoForUpdate();
        dto.setName(" ");
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/planet/update")
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
        PlanetDto dto = TestUtils.buildPlanetDtoForUpdate();
        dto.setVersion(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/planet/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_VERSION);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validate_nullStar() throws Exception {
        //given
        PlanetDto dto = TestUtils.buildPlanetDtoForUpdate();
        dto.setStar(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/planet/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_STAR);
        then(service).should(never()).add(any());
    }

    @Test
    void testUpdate_validate_nullStarId() throws Exception {
        //given
        PlanetDto dto = TestUtils.buildPlanetDtoForUpdate();
        dto.getStar().setId(null);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/planet/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_STAR_ID);
        then(service).should(never()).add(any());
    }

    @Test
    void testUpdate_dirtyFieldFixAndSuccessfulUpdate() throws Exception {
        //given
        PlanetDto inputDto = TestUtils.buildPlanetDtoForUpdate();
        Planet entity = modelMapper.map(inputDto, Planet.class);
        PlanetDto resultDto = modelMapper.map(entity, PlanetDto.class);

        //dirty input
        inputDto.setName(" name ");

        given(service.update(any())).willReturn(entity);
        //when
        MockHttpServletResponse response = mockMvc.perform(put("/planet/update")
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
        MockHttpServletResponse response = mockMvc.perform(delete("/planet/delete/{id}", id)).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }
}
